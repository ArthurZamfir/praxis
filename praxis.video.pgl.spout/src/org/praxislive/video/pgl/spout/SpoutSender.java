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
package org.praxislive.video.pgl.spout;

import org.praxislive.video.pgl.PGLContext;
import org.praxislive.video.pgl.PGLTextureSharer;
import processing.core.PImage;
import spout.Spout;

/**
 *
 * @author neilcsmith
 */
class SpoutSender implements PGLTextureSharer.Sender {
    
    private final Spout server;

    SpoutSender(PGLContext context, String serverID) {
        server = new Spout(context.parent());
        if (serverID.isEmpty()) {
            server.createSender("PraxisLIVE");
        } else {
            server.createSender(serverID);
        }
    }

    @Override
    public void sendFrame(PImage frame) {
        server.sendTexture(frame);
    }

    @Override
    public void dispose() {
//        server.closeSender();
        server.dispose();
    }
    
}
