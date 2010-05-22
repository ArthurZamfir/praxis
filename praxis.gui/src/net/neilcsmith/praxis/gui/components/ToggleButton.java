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
package net.neilcsmith.praxis.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import net.neilcsmith.praxis.core.Argument;
import net.neilcsmith.praxis.core.types.PString;
import net.neilcsmith.praxis.gui.SingleBindingGuiComponent;
import net.neilcsmith.praxis.gui.ControlBinding.Adaptor;
import net.neilcsmith.praxis.gui.ToggleButtonModelAdaptor;
import net.neilcsmith.praxis.impl.ArgumentProperty;
import net.neilcsmith.praxis.impl.StringProperty;

/**
 *
 * @author Neil C Smith
 */
public class ToggleButton extends SingleBindingGuiComponent {

    private JToggleButton button;
    private Argument onArg;
    private Argument offArg;
    private ToggleButtonModelAdaptor adaptor;
    private String label;

    public ToggleButton() {
        label = "";
        onArg = PString.EMPTY;
        offArg = PString.EMPTY;
        registerControl("on-value", ArgumentProperty.create(this, new OnBinding(), onArg));
        registerControl("off-value", ArgumentProperty.create(this, new OffBinding(), offArg));
        registerControl("label", StringProperty.create(this, new LabelBinding(), label));

    }

    @Override
    protected Adaptor getBindingAdaptor() {
        if (adaptor == null) {
            createComponentAndAdaptor();
        }
        return adaptor;
    }

    @Override
    protected JComponent createSwingComponent() {
        if (button == null) {
            createComponentAndAdaptor();
        }
        return button;
    }

    private void createComponentAndAdaptor() {
        button = new JToggleButton(label);
        adaptor = new ToggleButtonModelAdaptor(button.getModel());
        setAdaptorArguments();
        button.addAncestorListener(new AncestorListener() {

            public void ancestorAdded(AncestorEvent event) {
                adaptor.setActive(true);
            }

            public void ancestorRemoved(AncestorEvent event) {
                adaptor.setActive(false);
            }

            public void ancestorMoved(AncestorEvent event) {
                // no op
            }
        });
        button.setIcon(new LEDIcon(new Color(20,0,0)));
        button.setSelectedIcon(new LEDIcon(new Color(200,0,0)));
    }

    private void setAdaptorArguments() {
        if (adaptor != null) {
            adaptor.setOnArgument(onArg);
            adaptor.setOffArgument(offArg);
        }
    }

    private class LabelBinding implements StringProperty.Binding {

        public void setBoundValue(long time, String value) {
            label = value;
            if (button != null) {
                button.setText(value);
            }
        }

        public String getBoundValue() {
            return label;
        }
    }

    private class OnBinding implements ArgumentProperty.Binding {

        public void setBoundValue(long time, Argument value) {
            onArg = value;
            setAdaptorArguments();
        }

        public Argument getBoundValue() {
            return onArg;
        }



    }

    private class OffBinding implements ArgumentProperty.Binding {

        public void setBoundValue(long time, Argument value) {
            offArg = value;
            setAdaptorArguments();
        }

        public Argument getBoundValue() {
            return offArg;
        }

    }

    private class LEDIcon implements Icon {

        private final Color color;

        private LEDIcon(Color color) {
            this.color = color;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {

            Graphics2D g2d = (Graphics2D) g;
    
            Color oldColor = g2d.getColor();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(color);

            g2d.fillOval(x, y, getIconWidth(), getIconHeight());

            g2d.setColor(oldColor);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_DEFAULT);

        }

        public int getIconWidth() {
            return 8;
        }

        public int getIconHeight() {
            return 8;
        }

    }

}