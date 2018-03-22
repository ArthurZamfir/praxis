/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2018 Neil C Smith.
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

import org.praxislive.code.GenerateTemplate;

import org.praxislive.audio.code.AudioCodeDelegate;

// default imports
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import org.praxislive.core.*;
import org.praxislive.core.types.*;
import org.praxislive.code.userapi.*;
import static org.praxislive.code.userapi.Constants.*;
import org.praxislive.audio.code.userapi.*;
import static org.praxislive.audio.code.userapi.AudioConstants.*;

/**
 *
 * @author Neil C Smith - http://www.neilcsmith.net
 */
@GenerateTemplate(AudioFXReverb.TEMPLATE_PATH)
public class AudioFXReverb extends AudioCodeDelegate {
    
    final static String TEMPLATE_PATH = "resources/fx_reverb.pxj";

    // PXJ-BEGIN:body
    
    @In(1) AudioIn in1;
    @In(2) AudioIn in2;
    @Out(1) AudioOut out1;
    @Out(2) AudioOut out2;
    
    @UGen Freeverb r;
    
    @P(1) @Type.Number(min=0, max=1, def=0.5)
    Property roomSize;
    @P(2) @Type.Number(min=0, max=1, def=0.5)
    Property damp;
    @P(3) @Type.Number(min=0, max=1, def=0.5)
    Property width;
    @P(4) @Type.Number(min=0, max=1)
    Property wet;
    @P(5) @Type.Number(min=0, max=1, def=0.5)
    Property dry;
    
    @Override
    public void setup() {
        roomSize.link(r::roomSize);
        damp.link(r::damp);
        width.link(r::width);
        wet.link(r::wet);
        dry.link(r::dry);
        link(in1, r, out1);
        link(in2, r, out2);
    }
    
    // PXJ-END:body
    
}