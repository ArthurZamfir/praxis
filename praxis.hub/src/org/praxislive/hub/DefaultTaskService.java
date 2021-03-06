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
package org.praxislive.hub;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.praxislive.core.Value;
import org.praxislive.core.CallArguments;
import org.praxislive.core.Call;
import org.praxislive.core.Component;
import org.praxislive.core.PacketRouter;
import org.praxislive.core.VetoException;
import org.praxislive.core.ControlInfo;
import org.praxislive.core.services.TaskService;
import org.praxislive.core.services.TaskService.Task;
import org.praxislive.core.types.PReference;
import org.praxislive.impl.AbstractRoot;

/**
 *
 * @author Neil C Smith
 */
class DefaultTaskService extends AbstractRoot {

    private final static Logger LOG = Logger.getLogger(DefaultTaskService.class.getName());

    private ExecutorService threadService;
    private Map<Future<Value>, Call> futures;
    private List<Future> completed;

    public DefaultTaskService() {
        super(EnumSet.noneOf(Caps.class));
        threadService = Executors.newCachedThreadPool(new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
               Thread thr = new Thread(r);
               thr.setPriority(Thread.MIN_PRIORITY);
               return thr;
            }
        });
        ControlEx submitter = new SubmitControl();
        registerControl(TaskService.SUBMIT, submitter);
        registerProtocol(TaskService.class);
        futures = new HashMap<>();
        completed = new ArrayList<Future>();
    }


    @Override
    public void addChild(String id, Component child) throws VetoException {
        throw new VetoException();
    }
    
    @Override
    protected void update() {
            for (Future<Value> future : futures.keySet()) {
                if (future.isDone()) {
                    try {
                        Value value = future.get();
                        Call call = futures.get(future);
                        call = Call.createReturnCall(call, value);
                        getPacketRouter().route(call);
                        completed.add(future);
                    } catch (Exception ex) {
                        LOG.log(Level.FINEST, null, ex);
                        if (ex instanceof ExecutionException) {
                            Throwable t = ex.getCause();
                            if (t instanceof Exception) {
                                ex = (Exception) t;
                            }
                        }
                        Call call = futures.get(future);
                        call = Call.createErrorCall(call, PReference.wrap(ex));
                        getPacketRouter().route(call);
                        completed.add(future);
                    }
                }
            }
            while (! completed.isEmpty()) {
                futures.remove(completed.get(0));
                completed.remove(0);
            }
        }

    private class SubmitControl implements ControlEx {

        @Override
        public void call(Call call, PacketRouter router) throws Exception {
            switch (call.getType()) {
                case INVOKE :
                case INVOKE_QUIET :
                    submitTask(call);
                    break;
                default :
                    throw new IllegalArgumentException("Unexpected call\n" + call);
            }
        }

        private void submitTask(Call call) throws Exception {
            CallArguments args = call.getArgs();
            if (args.getSize() == 1) {
                Value arg = args.get(0);
                if (arg instanceof PReference) {
                    Object ref = ((PReference) arg).getReference();
                    if (ref instanceof Task) {
                        final Task task = (Task) ref;
                        Future<Value> future = threadService.submit(
                                new Callable<Value>() {

                                    @Override
                                    public Value call() throws Exception {
                                        return task.execute();
                                    }
                                });
                        futures.put(future, call);
                    }
                }
            } else {
                throw new IllegalArgumentException();
            }
            
        }

        @Override
        public ControlInfo getInfo() {
            return TaskService.SUBMIT_INFO;
        }

    }


}
