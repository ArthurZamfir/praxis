/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008/09 - Neil C Smith. All rights reserved.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details.
 * 
 * You should have received a copy of the GNU General Public License version 2
 * along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please visit http://neilcsmith.net if you need additional information or
 * have any questions.
 */

package net.neilcsmith.praxis.gui;

import net.neilcsmith.praxis.core.ControlAddress;
import net.neilcsmith.praxis.core.Root;

/**
 * Extended Root interface for GUI.
 *
 * @author Neil C Smith
 */
public interface GuiRoot extends Root {
    
    /**
     * Bind adaptor to the binding for the given ControlAddress.
     *
     * @param address
     * @param adaptor
     */
    public void bind(ControlAddress address, ControlBinding.Adaptor adaptor);
    
    /**
     * Unbind adaptor from its binding.  If the adaptor is not bound, this method
     * will do nothing.
     *
     * @param adaptor
     */
    public void unbind(ControlBinding.Adaptor adaptor);

}