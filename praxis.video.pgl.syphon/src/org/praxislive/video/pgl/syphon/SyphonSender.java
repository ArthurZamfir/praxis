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
package org.praxislive.video.pgl.syphon;

import codeanticode.syphon.SyphonServer;
import org.praxislive.video.pgl.PGLContext;
import org.praxislive.video.pgl.PGLTextureSharer;
import processing.core.PImage;

/**
 *
 * @author neilcsmith
 */
class SyphonSender implements PGLTextureSharer.Sender {
    
    private final SyphonServer server;

    SyphonSender(PGLContext context, String serverID) {
        if (serverID.isEmpty()) {
            server = new SyphonServer(context.parent(), "PraxisLIVE");
        } else {
            server = new SyphonServer(context.parent(), serverID);
        }
    }

    @Override
    public void sendFrame(PImage frame) {
        server.sendImage(frame);
    }

    @Override
    public void dispose() {
        server.stop();
    }
    
}
