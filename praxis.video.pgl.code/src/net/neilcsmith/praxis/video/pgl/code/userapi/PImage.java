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
 *
 *
 * Parts of the API of this package, as well as some of the code, is derived from
 * the Processing project (http://processing.org)
 *
 * Copyright (c) 2004-09 Ben Fry and Casey Reas
 * Copyright (c) 2001-04 Massachusetts Institute of Technology
 *
 */

package net.neilcsmith.praxis.video.pgl.code.userapi;

import java.util.Optional;
import net.neilcsmith.praxis.video.pgl.PGLContext;


/**
 *
 * @author Neil C Smith <http://neilcsmith.net>
 */
public abstract class PImage {
    
    public /*final*/ int width;
    public /*final*/ int height;
    
    PImage() {
        
    }
    
    public PImage(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    /**
     * Search for an instance of the given type.
     * @param <T>
     * @param type class to search for
     * @return Optional wrapping the result if found, or empty if not
     */
    public <T> Optional<T> find(Class<T> type) {
        return Optional.empty();
    }
    
    
    protected abstract processing.core.PImage unwrap(PGLContext context);
    
}
