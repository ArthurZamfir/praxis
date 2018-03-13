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
package org.praxislive.video.factory;

import org.praxislive.code.AbstractComponentFactory;
import org.praxislive.core.ComponentType;
import org.praxislive.core.services.ComponentFactory;
import org.praxislive.core.services.ComponentFactoryProvider;
import org.praxislive.meta.TypeRewriter;
import org.praxislive.video.code.VideoCodeDelegate;
import org.praxislive.video.code.VideoCodeFactory;

/**
 *
 * @author Neil C Smith <http://neilcsmith.net>
 */
public class VideoComponents implements ComponentFactoryProvider {
    private final static Factory instance = new Factory();

    @Override
    public ComponentFactory getFactory() {
        return instance;
    }

    private static class Factory extends AbstractComponentFactory {

        private Factory() {
            build();
        }

        private void build() {
            
            // custom
            add("video:custom", VideoCustom.class, VideoCustom.TEMPLATE_PATH);
            
            // built-in
            
            // CORE VIDEO
            add("video:snapshot", VideoSnapshot.class, VideoSnapshot.TEMPLATE_PATH);
            add("video:still", VideoStill.class, VideoStill.TEMPLATE_PATH);
            
            // ANALYSIS
            add("video:analysis:frame-delay", VideoAnalysisFrameDelay.class,
                    VideoAnalysisFrameDelay.TEMPLATE_PATH);
            add("video:analysis:difference", VideoAnalysisDifference.class,
                    VideoAnalysisDifference.TEMPLATE_PATH);
            
            // FX
            add("video:fx:blur", VideoFXBlur.class, VideoFXBlur.TEMPLATE_PATH);
            
            // SOURCE
            add("video:source:noise", VideoSourceNoise.class, VideoSourceNoise.TEMPLATE_PATH);
            
        }

        private void add(String type, Class<? extends VideoCodeDelegate> cls, String path) {
            add(data(
                    new VideoCodeFactory(ComponentType.create(type), cls, source(path))
            ));
        }
        
    }
}