/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 - Neil C Smith. All rights reserved.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details.
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please visit http://neilcsmith.net if you need additional information or
 * have any questions.
 */
package net.neilcsmith.praxis.video.components;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.neilcsmith.praxis.core.Port;
import net.neilcsmith.praxis.core.Root;
import net.neilcsmith.praxis.core.Root.State;
import net.neilcsmith.praxis.impl.AbstractRootStateComponent;
import net.neilcsmith.praxis.impl.FloatProperty;
import net.neilcsmith.praxis.impl.TriggerControl;
import net.neilcsmith.praxis.video.DefaultVideoOutputPort;
import net.neilcsmith.ripl.components.Delegator;
import net.neilcsmith.ripl.delegates.VideoDelegate;
import net.neilcsmith.ripl.delegates.VideoDelegate.StateException;

/**
 *
 * @author Neil C Smith
 */
public class VideoPlayer extends AbstractRootStateComponent {

    VideoDelegate video;
    Delegator delegator;
    VideoDelegateLoader loader;

    public VideoPlayer() {
        delegator = new Delegator();
        registerPort(Port.OUT, new DefaultVideoOutputPort(this, delegator));
        loader = new VideoDelegateLoader(this, new VideoBinding());
        registerControl("uri", loader);
        TriggerControl play = TriggerControl.create(this, new PlayTrigger());
        registerControl("play", play);
        TriggerControl pause = TriggerControl.create(this, new PauseTrigger());
        registerControl("pause", pause);
        FloatProperty position = FloatProperty.create(this, new PositionBinding(), 0, 1, 0);
        registerControl("position", position);


    }

    private class PositionBinding implements FloatProperty.Binding {

        public void setBoundValue(long time, double value) {
            if (video != null && video.isSeekable()) {
                long duration = video.getDuration();
                if (duration > 0) {
                    long position = (long) (value * duration);
                    video.setPosition(position);
                }
            }
        }

        public double getBoundValue() {
            if (video == null) {
                return 0;
            } else {
                long duration = video.getDuration();
                long position = video.getPosition();
                double value = (double) position / duration;
                if (value < 0 || value > 1) {
                    return 0;
                } else {
                    return value;
                }
            }
        }
    }

    private class PlayTrigger implements TriggerControl.Binding {

        public void trigger(long time) {
            if (video != null) {
                try {
                    video.play();
                } catch (StateException ex) {
                    Logger.getLogger(VideoPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private class PauseTrigger implements TriggerControl.Binding {

        public void trigger(long time) {
            if (video != null) {
                try {
                    video.pause();
                } catch (StateException ex) {
                    Logger.getLogger(VideoPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private class VideoBinding implements VideoDelegateLoader.Listener {

        public void setDelegate(VideoDelegate delegate) {
            if (video != null) {
                video.dispose();
            }
            video = delegate;
            delegator.setDelegate(delegate);
            
        }

        public void delegateLoaded(VideoDelegateLoader source) {
            setDelegate(source.getDelegate());
        }

        public void delegateError(VideoDelegateLoader source) {
            
        }
        
    }
    
//    private class VideoLoader extends ResourceLoader<VideoDelegate> {
//
//        VideoLoader() {
//            super(VideoPlayer.this, VideoDelegate.class);
//        }
//
//        @Override
//        protected Task getLoadTask(PUri uri) {
//            return getDelegateLoaderTask(uri);
//        }
//
//        @Override
//        protected void setResource(VideoDelegate resource) {
//            if (video != null) {
//                video.dispose();
//                video = null;
//            }
//            if (resource == null) {
//                delegator.setDelegate(null);
//
//            } else {
//                delegator.setDelegate(resource);
//                video = resource;
//            }
//        }

//        @Override
//        protected void setResource(Argument arg) {
//            if (arg == null) {
//                delegator.setDelegate(null);
//                if (video != null) {
//                    video.dispose();
//                    video = null;
//                }
//            } else {
//                try {
//                    PReference ref = PReference.coerce(arg);
//                    VideoDelegate del = (VideoDelegate) ref.getReference();
//                    video = del;
//                    delegator.setDelegate(del);
//                } catch (Exception ex) {
//                    Logger.getLogger(VideoPlayer.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//
//
//        }
//    }
//
//    protected abstract Task getDelegateLoaderTask(PUri uri);
//
    public void rootStateChanged(Root source, State state) {
        if (state == State.ACTIVE_IDLE) {
            if (video != null) {
                try {
                    video.stop();
                } catch (StateException ex) {
                    // no op
                }
            }
        } else if (state == State.TERMINATING) {
            if (video != null) {
                delegator.setDelegate(null);
                video.dispose();
                video = null;

            }
        }
    }
    
}
