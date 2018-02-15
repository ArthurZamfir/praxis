/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2016 Neil C Smith.
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
package org.praxislive.audio.components;

import org.praxislive.audio.components.analysis.Level;
import org.praxislive.audio.components.container.AudioContainerInput;
import org.praxislive.audio.components.container.AudioContainerOutput;
import org.praxislive.audio.components.mix.XFader;
import org.praxislive.audio.components.sampling.Looper;
import org.praxislive.core.services.ComponentFactory;
import org.praxislive.core.services.ComponentFactoryProvider;
import org.praxislive.impl.AbstractComponentFactory;

/**
 *
 * @author Neil C Smith (http://neilcsmith.net)
 */
public class AudioFactoryProvider implements ComponentFactoryProvider {
    
    private final static ComponentFactory factory = new Factory();
    
    public ComponentFactory getFactory() {
        return factory;
    }
    
    private static class Factory extends AbstractComponentFactory {
        
        private Factory() {
            build();
        }
        
        private void build() {
            //ROOT
            addRoot("root:audio", DefaultAudioRoot.class);

            //COMPONENTS
            addComponent("audio:input", AudioInput.class);
            addComponent("audio:output", AudioOutput.class);
            addComponent("audio:analysis:level", Level.class);
            addComponent("audio:mix:xfader", XFader.class);
            addComponent("audio:sampling:looper", Looper.class);
            
            addComponent("audio:container:in", data(AudioContainerInput.class));
            addComponent("audio:container:out", data(AudioContainerOutput.class));
            
        }
    }
}
