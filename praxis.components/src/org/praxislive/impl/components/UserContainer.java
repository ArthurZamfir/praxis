/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2018 Neil C Smith.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3 only, as
 * published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * version 3 for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License version 3
 * along with this work; if not, see http://www.gnu.org/licenses/
 * 
 * 
 * Please visit https://www.praxislive.org if you need additional information or
 * have any questions.
 */
package org.praxislive.impl.components;

import org.praxislive.impl.ContainerContext;
import org.praxislive.core.Control;
import org.praxislive.core.Lookup;
import org.praxislive.core.Port;
import org.praxislive.impl.RegistrationException;
import org.praxislive.impl.AbstractContainer;
import org.praxislive.impl.InstanceLookup;

/**
 *
 * @author Neil C Smith
 */
public class UserContainer extends AbstractContainer {
    
    private final Context context;
    private Lookup lookup;
    
    
    public UserContainer() {
        context = new Context();
        markDynamic();
    }

    @Override
    public Lookup getLookup() {
        if (lookup == null) {
            lookup = InstanceLookup.create(super.getLookup(), context);
        }
        return lookup;
    }

    @Override
    public void hierarchyChanged() {
        super.hierarchyChanged();
        lookup = null;
    }
    
    
    
    
    private class Context extends ContainerContext {

        @Override
        public void registerControl(String id, ControlEx control) throws RegistrationException {
            try {
                UserContainer.this.registerControl(id, control);
            } catch (Exception ex) {
                throw new RegistrationException(ex);
            }
        }

        @Override
        public void unregisterControl(String id, ControlEx control) {
            // check control is correct
            UserContainer.this.unregisterControl(id);
        }

        @Override
        public void registerPort(String id, PortEx port) throws RegistrationException {
            try {
                UserContainer.this.registerPort(id, port);
            } catch (Exception ex) {
                throw new RegistrationException(ex);
            }
        }

        @Override
        public void unregisterPort(String id, PortEx port) {
            // check port is correct
            UserContainer.this.unregisterPort(id);
        }

        @Override
        public void refreshControlInfo(String id, ControlEx control) {
            UserContainer.this.refreshControlInfo(id);
        }

        @Override
        public void refreshPortInfo(String id, PortEx port) {
            UserContainer.this.refreshPortInfo(id);
        }
        
    }
    
}
