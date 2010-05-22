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

import java.awt.Dimension;
import java.net.URI;
import java.util.logging.Logger;
import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.core.ControlPort;
import net.neilcsmith.praxis.core.Port;
import net.neilcsmith.praxis.core.Task;
import net.neilcsmith.praxis.core.types.PReference;
import net.neilcsmith.praxis.core.types.PUri;
import net.neilcsmith.praxis.impl.AbstractComponent;
import net.neilcsmith.praxis.impl.DefaultControlOutputPort;
import net.neilcsmith.praxis.impl.FloatProperty;
import net.neilcsmith.praxis.impl.ResourceLoader;
import net.neilcsmith.praxis.impl.StringProperty;
import net.neilcsmith.praxis.video.DefaultVideoInputPort;
import net.neilcsmith.praxis.video.DefaultVideoOutputPort;
//import net.neilcsmith.ripl.components.Still;
import net.neilcsmith.ripl.components.Delegator;
import net.neilcsmith.ripl.utils.ResizeMode;
import net.neilcsmith.ripl.delegates.ImageDelegate;

/**
 *
 * @author Neil C Smith
 */
public class Still extends AbstractComponent {

    private static Logger logger = Logger.getLogger(Still.class.getName());
    private Delegator delegator;
    private ImageDelegate delegate;
    private ResizeMode resizeMode;
    private StringProperty resizeType;
    private FloatProperty alignX;
    private FloatProperty alignY;
    private ControlPort.Output rdyPort;
    private ControlPort.Output errPort;

    public Still() {
        delegator = new Delegator();
        resizeMode = new ResizeMode(ResizeMode.Type.Stretch, 0.5, 0.5);
        resizeType = createTypeControl();
        alignX = FloatProperty.create(this, new AlignXBinding(), 0, 1, 0.5);
        alignY = FloatProperty.create(this, new AlignYBinding(), 0, 1, 0.5);
        DelegateLoader loader = new DelegateLoader();
        registerControl("resize-mode", resizeType);
        registerControl("align-x", alignX);
        registerControl("align-y", alignY);
        registerControl("uri", loader);
        registerPort(Port.IN, new DefaultVideoInputPort(this, delegator));
        registerPort(Port.OUT, new DefaultVideoOutputPort(this, delegator));
        registerPort("uri", loader.getInputPort());
        rdyPort = new DefaultControlOutputPort(this);
        registerPort("ready", rdyPort);
        errPort = new DefaultControlOutputPort(this);
        registerPort("error", errPort);
//        registerPort("ready", loader.getCompletePort());
//        registerPort("error", loader.getErrorPort());

    }

    private void setDelegate(ImageDelegate delegate) {
        this.delegate = delegate;
        if (delegate != null && !delegate.getResizeMode().equals(resizeMode)) {
            delegate.setResizeMode(resizeMode);
        }
        delegator.setDelegate(delegate);
    }

    private StringProperty createTypeControl() {
        ResizeMode.Type[] types = ResizeMode.Type.values();
        String[] allowed = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            allowed[i] = types[i].name();
        }
        return StringProperty.create(this, new ResizeTypeBinding(), allowed,
                resizeMode.getType().name());
    }

    private class ResizeTypeBinding implements StringProperty.Binding {

        public void setBoundValue(long time, String value) {
            ResizeMode.Type type = ResizeMode.Type.valueOf(value);
            if (resizeMode.getType() != type) {
                resizeMode = new ResizeMode(type, resizeMode.getHorizontalAlignment(),
                        resizeMode.getVerticalAlignment());
                if (delegate != null) {
                    delegate.setResizeMode(resizeMode);
                }
            }
        }

        public String getBoundValue() {
            return resizeMode.getType().name();
        }
    }
    
    private class AlignXBinding implements FloatProperty.Binding {

        public void setBoundValue(long time, double value) {
            if (value != resizeMode.getHorizontalAlignment()) {
                resizeMode = new ResizeMode(resizeMode.getType(), value,
                        resizeMode.getVerticalAlignment());
                if (delegate != null) {
                    delegate.setResizeMode(resizeMode);
                }
            }
        }

        public double getBoundValue() {
            return resizeMode.getHorizontalAlignment();
        }
        
    }
    
    private class AlignYBinding implements FloatProperty.Binding {

        public void setBoundValue(long time, double value) {
            if (value != resizeMode.getVerticalAlignment()) {
                resizeMode = new ResizeMode(resizeMode.getType(),
                        resizeMode.getHorizontalAlignment(), value);
                if (delegate != null) {
                    delegate.setResizeMode(resizeMode);
                }
            }
        }

        public double getBoundValue() {
            return resizeMode.getVerticalAlignment();
        }
        
    }

//    public void propertyChanged(Property source) {
//        logger.info("Property change message from " + source);
//        ResizeMode mode = null;
//        ResizeMode oldMode = resizeMode;
//        if (source == resizeType) {
//            ResizeMode.Type type = ResizeMode.Type.valueOf(resizeType.getValue());
//            mode = new ResizeMode(type, oldMode.getHorizontalAlignment(),
//                    oldMode.getVerticalAlignment());
//        } else if (source == alignX) {
//            double oldX = oldMode.getHorizontalAlignment();
//            double newX = alignX.getValue();
//            if (newX != oldX) {
//                mode = new ResizeMode(oldMode.getType(), newX,
//                        oldMode.getVerticalAlignment());
//            }
//        } else {
//            double oldY = oldMode.getVerticalAlignment();
//            double newY = alignY.getValue();
//            if (newY != oldY) {
//                mode = new ResizeMode(oldMode.getType(),
//                        oldMode.getHorizontalAlignment(), newY);
//            }
//        }
//        if (mode != null) {
//            resizeMode = mode;
//            if (delegate != null) {
//                delegate.setResizeMode(mode);
//            }
//        }
//    }

//    private class ImageBinding implements ImageLoader.Binding {
//
//        public void setImage(BufferedImage image) {
//            im.setImage(image);
//        }
//        
//    }
    private class DelegateLoader extends ResourceLoader<ImageDelegate> {

        DelegateLoader() {
            super(Still.this, ImageDelegate.class);
        }
//
//        @Override
//        protected Task getLoadTask(PUri uri) {
//            
//        }

//        @Override
//        protected void setResource(Argument arg) {
//            if (arg == null) {
//                setDelegate(null);
//            } else {
//                if (arg instanceof PReference) {
//                    Object o = ((PReference) arg).getReference();
//                    if (o instanceof ImageDelegate) {
//                        setDelegate((ImageDelegate) o);
//                    }
//
//                }
//            }
//        }

//        @Override
//        protected void setResource(ImageDelegate resource) {
//            setDelegate(resource);
//        }

        @Override
        protected Task getLoadTask(Argument identifier) {
            return new LoaderTask(identifier, resizeMode, delegator.getCurrentDimensions());
        }

        @Override
        protected void resourceLoaded() {
            setDelegate(getResource());
            rdyPort.send(getRoot().getTime());
        }

        @Override
        protected void resourceError() {
            errPort.send(getRoot().getTime());
        }
    }

    private class LoaderTask implements Task {

        Argument uri;
        ResizeMode mode;
        Dimension guide;

        LoaderTask(Argument uri, ResizeMode mode, Dimension guide) {
            this.uri = uri;
            this.mode = mode;
            this.guide = guide;
        }

        public Argument execute() throws Exception {
            URI loc = PUri.coerce(uri).value();
            ImageDelegate del = ImageDelegate.create(loc, mode, guide);
            return PReference.wrap(del);
        }
    }
}