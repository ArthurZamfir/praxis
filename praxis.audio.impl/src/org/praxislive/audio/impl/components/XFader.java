/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2018 Neil C Smith. All rights reserved.
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
 *
 */

package org.praxislive.audio.impl.components;

import org.praxislive.audio.impl.AudioInputPortEx;
import org.praxislive.audio.impl.AudioOutputPortEx;
import org.praxislive.core.Port;
import org.praxislive.impl.AbstractComponent;
import org.praxislive.impl.NumberProperty;
import org.jaudiolibs.audioops.impl.GainOp;
import org.jaudiolibs.pipes.impl.Mixer;
import org.jaudiolibs.pipes.impl.OpHolder;

/**
 *
 * @author Neil C Smith
 */
public class XFader extends AbstractComponent {

    GainOp g1;
    GainOp g2;
    Mixer mixer;

    public XFader() {
        g1 = new GainOp();
        g1.setGain(1);
        g2 = new GainOp();
        g2.setGain(0);
        OpHolder g1h = new OpHolder(g1);
        OpHolder g2h = new OpHolder(g2);
        mixer = new Mixer(2);
        try {
            mixer.addSource(g1h);
            mixer.addSource(g2h);
        } catch (Exception ex) {
            throw new Error();
        }
        registerPort(PortEx.IN + "-1", new AudioInputPortEx(g1h));
        registerPort(PortEx.IN + "-2", new AudioInputPortEx(g2h));
        registerPort(PortEx.OUT, new AudioOutputPortEx(mixer));
        NumberProperty mix = NumberProperty.create( new MixBinding(), 0, 1, 0);
        registerControl("mix", mix);
        registerPort("mix", mix.createPort());
        
    }

    private class MixBinding implements NumberProperty.Binding {

        public void setBoundValue(long time, double value) {
            g1.setGain((float) (1 - value));
            g2.setGain((float) value);
        }

        public double getBoundValue() {
            return g2.getGain();
        }

    }

}
