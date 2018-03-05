/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Neil C Smith.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License version 3
 * along with this work; if not, see http://www.gnu.org/licenses/
 * 
 *
 * Please visit http://neilcsmith.net if you need additional information or
 * have any questions.
 */
package org.praxislive.script.commands;

import org.praxislive.script.impl.AbstractSingleCallFrame;
import java.util.Map;
import org.praxislive.core.Call;
import org.praxislive.core.CallArguments;
import org.praxislive.core.ComponentAddress;
import org.praxislive.core.ControlAddress;
import org.praxislive.core.PortAddress;
import org.praxislive.core.protocols.ContainerProtocol;
import org.praxislive.core.types.PString;
import org.praxislive.script.Command;
import org.praxislive.script.CommandInstaller;
import org.praxislive.script.Env;
import org.praxislive.script.ExecutionException;
import org.praxislive.script.Namespace;
import org.praxislive.script.StackFrame;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class ConnectionCmds implements CommandInstaller {

    private final static ConnectionCmds instance = new ConnectionCmds();
    private final static Connect CONNECT = new Connect();
    private final static Disconnect DISCONNECT = new Disconnect();

    private ConnectionCmds() {
    }

    public void install(Map<String, Command> commands) {
        commands.put("connect", CONNECT);
        commands.put("~", CONNECT);
        commands.put("disconnect", DISCONNECT);
        commands.put("!~", DISCONNECT);
    }

    public final static ConnectionCmds getInstance() {
        return instance;
    }

    private static class Connect implements Command {

        public StackFrame createStackFrame(Namespace namespace, CallArguments args) throws ExecutionException {
            return new ConnectionStackFrame(namespace, args, true);
        }
    }
    
    private static class Disconnect implements Command {
        
        public StackFrame createStackFrame(Namespace namespace, CallArguments args) throws ExecutionException {
            return new ConnectionStackFrame(namespace, args, false);
        }
        
    }

    private static class ConnectionStackFrame extends AbstractSingleCallFrame {

        private boolean connect;

        private ConnectionStackFrame(Namespace namespace, CallArguments args, boolean connect) {
            super(namespace, args);
            this.connect = connect;
        }

        @Override
        protected Call createCall(Env env, CallArguments args) throws Exception {
            PortAddress p1 = PortAddress.coerce(args.get(0));
            PortAddress p2 = PortAddress.coerce(args.get(1));
            ComponentAddress c1 = p1.getComponentAddress();
            ComponentAddress c2 = p2.getComponentAddress();
            ComponentAddress container = c1.getParentAddress();
            if (container == null || !c2.getParentAddress().equals(container)) {
                throw new IllegalArgumentException("Ports don't share a common parent");
            }
            CallArguments sendArgs = CallArguments.create(
                    PString.valueOf(c1.getComponentID(c1.getDepth() - 1)),
                    PString.valueOf(p1.getID()),
                    PString.valueOf(c2.getComponentID(c1.getDepth() - 1)),
                    PString.valueOf(p2.getID()));
            ControlAddress to = ControlAddress.create(container,
                    connect ? ContainerProtocol.CONNECT : ContainerProtocol.DISCONNECT);
            return Call.createCall(to, env.getAddress(), env.getTime(), sendArgs);

        }
    }
}
