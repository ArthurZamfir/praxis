/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2015 Neil C Smith.
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
package net.neilcsmith.praxis.video.gst1.components;

import net.neilcsmith.praxis.core.ComponentFactory;
import net.neilcsmith.praxis.core.ComponentFactoryProvider;
import net.neilcsmith.praxis.impl.AbstractComponentFactory;
import net.neilcsmith.praxis.video.VideoSettings;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class GStreamerFactoryProvider implements ComponentFactoryProvider {

    private final static boolean ACTIVE_LIB
            = "gstreamer-1.x".equals(VideoSettings.getMediaLib());

    private final static ComponentFactory factory = new Factory();

    @Override
    public ComponentFactory getFactory() {
        return factory;
    }

    private static class Factory extends AbstractComponentFactory {

        private Factory() {
            build();
        }

        private void build() {
            if (ACTIVE_LIB) {
                addComponent("video:player", VideoPlayer.class);
                addComponent("video:capture", VideoCapture.class);
            }
        }
    }
}