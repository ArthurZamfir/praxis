/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2014 Neil C Smith.
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

package net.neilcsmith.praxis.core.code;

import net.neilcsmith.praxis.code.CodeUtils;
import net.neilcsmith.praxis.compiler.ClassBodyContext;

/**
 *
 * @author Neil C Smith <http://neilcsmith.net>
 */
public class CoreBodyContext extends ClassBodyContext<CoreCodeDelegate> {
    
    public final static String TEMPLATE =
            CodeUtils.load(CoreBodyContext.class, "resources/core_template.pxj");
            
    
    public CoreBodyContext() {
        super(CoreCodeDelegate.class);
    }

    @Override
    public String[] getDefaultImports() {
        return CodeUtils.defaultImports();
    }

}