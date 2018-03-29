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
package org.praxislive.video.components;

import org.praxislive.code.GenerateTemplate;

import org.praxislive.video.code.VideoCodeDelegate;

// default imports
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import org.praxislive.core.*;
import org.praxislive.core.types.*;
import org.praxislive.code.userapi.*;
import static org.praxislive.code.userapi.Constants.*;
import org.praxislive.video.code.userapi.*;
import static org.praxislive.video.code.userapi.VideoConstants.*;

/**
 *
 * @author Neil C Smith - http://www.neilcsmith.net
 */
@GenerateTemplate(VideoComposite.TEMPLATE_PATH)
public class VideoComposite extends VideoCodeDelegate {
    
    final static String TEMPLATE_PATH = "resources/composite.pxj";

    // PXJ-BEGIN:body
    
    @In(1) PImage in;
    @In(2) PImage src;
    
    @P(1)
    BlendMode mode;
    @P(2) @Type.Number(min = 0, max = 1, def = 1)
    double mix;
    @P(3)
    boolean forceAlpha;
    
    @Override
    public void init() {
        attachAlphaQuery("src", outAlpha -> outAlpha || forceAlpha);
    }
    
    @Override
    public void draw() {
        copy(in);
        release(in);
        blendMode(mode, mix);
        image(src, 0, 0);
    }
    
    // PXJ-END:body
    
}
