/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2009 Neil C Smith. All rights reserved.
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
 *
 */
package net.neilcsmith.praxis.gui;

import java.awt.EventQueue;
import javax.swing.JComponent;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ConstraintParser;
import net.neilcsmith.praxis.core.Container;
import net.neilcsmith.praxis.core.ParentVetoException;
import net.neilcsmith.praxis.impl.AbstractComponent;
import net.neilcsmith.praxis.impl.StringProperty;

/**
 * Abstract superclass for default GuiComponent model.
 * @author Neil C Smith
 */
public abstract class AbstractGuiComponent extends AbstractComponent implements GuiComponent {

    private JComponent component;
    private LayoutBinding layout;

    protected AbstractGuiComponent() {
        layout = new LayoutBinding();
        registerControl("layout", StringProperty.create(this, layout, layout.layoutString));
    }

    public final JComponent getSwingComponent() {
        if (EventQueue.isDispatchThread()) {
            if (component == null) {
                component = createSwingComponent();
                if (layout.constraint != null) {
                    component.putClientProperty(ClientKeys.LayoutConstraint, layout.constraint);
                }
            }
            return component;
        } else {
            return null;
        }
    }

    @Override
    public void parentNotify(Container parent) throws ParentVetoException {
        if (EventQueue.isDispatchThread()) {
            super.parentNotify(parent);
            // call getSwingComponent() early to ensure JComponent creation
            getSwingComponent();
        } else {
            throw new ParentVetoException("Trying to install GUI component in GUI incompatible container.");
        }

    }

    protected abstract JComponent createSwingComponent();

    private class LayoutBinding implements StringProperty.Binding {

        private String layoutString = "";
        private CC constraint = null;

        public void setBoundValue(long time, String value) {
            if (value.isEmpty()) {
                constraint = null;
            } else {
                constraint = ConstraintParser.parseComponentConstraint(value);
            }

            if (EventQueue.isDispatchThread() && component != null) {
                component.putClientProperty(ClientKeys.LayoutConstraint, constraint);
            }

            layoutString = value;

        }

        public String getBoundValue() {
            return layoutString;
        }
    }
}