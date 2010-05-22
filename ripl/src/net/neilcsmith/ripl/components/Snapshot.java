/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2008 - Neil C Smith. All rights reserved.
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
package net.neilcsmith.ripl.components;

import net.neilcsmith.ripl.Source;
import net.neilcsmith.ripl.Surface;
import net.neilcsmith.ripl.SurfaceCapabilities;
import net.neilcsmith.ripl.impl.SingleInOut;
import net.neilcsmith.ripl.ops.Blend;
import net.neilcsmith.ripl.ops.Blit;
//import net.neilcsmith.ripl.delegates.SurfaceComposite;

/**
 *
 * @author Neil C Smith
 */
public class Snapshot extends SingleInOut {

    private static SurfaceCapabilities stableCaps = new SurfaceCapabilities(true);
    private boolean capturing;
    private boolean fading;
    private long captureTime;
    private double fadeTime;
    private long activeFadeTime;
    private double activeMix;
    private double mix = 1;
    private Surface fg;
    private Surface bg;
    private Surface temp;

    public void setMix(double mix) {
        if (mix < 0 || mix > 1) {
            throw new IllegalArgumentException();
        }
        this.mix = mix;
    }

    public double getMix() {
        return mix;
    }

    public void trigger() {
        capturing = true;
    }

    public void setFadeTime(double secs) {
        if (secs < 0) {
            throw new IllegalArgumentException();
        }
        fadeTime = secs;
    }

    public double getFadeTime() {
        return fadeTime;
    }

    @Override
    protected void process(Surface surface, boolean rendering) {
        if (capturing) {
            capture(surface);
        } else if (rendering) {
            render(surface);
        }
    }

    private void mix(Surface src, Surface destIn, Surface destOut, double amount) {
        float fAmount = (float) amount;
        if (destOut.hasAlpha()) {
            Surface dest;
            if (destIn == destOut) {
                dest = temp;
                if (dest == null || !destOut.checkCompatible(dest, true, true)) {
                    dest = destOut.createSurface(null);
                }
                temp = dest;
            } else {
                dest = destOut;
            }
            dest.clear();


//            BlendComposite comp = BlendComposite.getInstance(BlendComposite.Mode.AddPin, 1 - fAmount);
//            SurfaceGraphics g = dest.getGraphics();
//            g.setComposite(comp);
//            g.drawSurface(destIn, 0, 0);
//            g.setComposite(comp.derive(fAmount));
//            g.drawSurface(src, 0, 0);

            dest.process(Blit.op(Blend.ADD.derive(1 - fAmount)), destIn);
            dest.process(Blit.op(Blend.ADD.derive(fAmount)), src);


            if (dest != destOut) {
                destOut.clear();
                destOut.copy(dest);
                dest.release();
            }
        } else {
//            SurfaceGraphics g = destOut.getGraphics();
            if (destIn != destOut) {
                destOut.copy(destIn);
            }
//            g.setComposite(AlphaComposite.SrcOver.derive(fAmount));
//            g.drawSurface(src,0,0);
            destOut.process(Blit.op(Blend.NORMAL.derive(fAmount)), src);
        }
    }

    private void capture(Surface surface) {
        checkSurfaces(surface);
        long time = getTime();
        if (fading) {
            // currently in middle of fade so set bg to current state
            double percent = (time - captureTime) / (double) activeFadeTime;
            if (percent < 0 || percent > 1) {
                if (activeMix == 1) {
                    swapSurfaces();
                } else {
//                    cmp = cmp.derive(activeMix);
//                    cmp.process(fg, bg, bg, 0, 0);
                    mix(fg, bg, bg, activeMix);
                }
            } else {
//                cmp = cmp.derive(percent * activeMix);
//                cmp.process(fg, bg, bg, 0, 0);
                mix(fg, bg, bg, percent * activeMix);
            }
        }
// already done in render ?       else {
//            swapSurfaces();
//        }
        activeFadeTime = (long) (fadeTime * 1e9);
        activeMix = mix;
        captureTime = time;
        if (activeFadeTime == 0) {
            if (activeMix == 1) {
                bg.clear();
//                bg.getGraphics().drawSurface(surface, 0, 0);
                bg.copy(surface);
                fg.release();
            } else {
//                cmp = cmp.derive(activeMix);
//                cmp.process(surface, bg, bg, 0, 0);
//                surface.getGraphics().drawSurface(bg, 0, 0);
                mix(surface, bg, bg, activeMix);
//                surface.getGraphics().drawSurface(bg, 0, 0);
                surface.copy(bg);
                fg.release();
            }
            fading = false;
        } else {
            fg.clear();
//            fg.getGraphics().drawSurface(surface, 0, 0);
            fg.copy(surface);
            surface.clear();
//            surface.getGraphics().drawSurface(bg, 0, 0);
            surface.copy(bg);
            fading = true;
        }
        capturing = false;
    }

    private void render(Surface surface) {
        checkSurfaces(surface);
        surface.clear();


        long time = getTime();
        if (fading) {
            double percent = (time - captureTime) / (double) activeFadeTime;
            if (percent < 0 || percent > 1) {
                if (activeMix == 1) {
                    swapSurfaces();
                } else {
//                    cmp = cmp.derive(activeMix);
//                    cmp.process(fg, bg, bg, 0, 0);
                    mix(fg, bg, bg, activeMix);
                }
                fading = false;
//                surface.getGraphics().drawSurface(bg, 0, 0);
                surface.copy(bg);
            } else {
//                cmp = cmp.derive(percent * activeMix);
////                surface.getGraphics().drawSurface(bg, 0, 0);
//                cmp.process(fg, bg, surface, 0, 0);
                mix(fg, bg, surface, percent * activeMix);
            }
        } else {
//            surface.getGraphics().drawSurface(bg, 0, 0);
            surface.copy(bg);
        }
    }

    private void swapSurfaces() {
        Surface s = fg;
        fg = bg;
        bg = s;
    }

    private void checkSurfaces(Surface surface) {
        if (fg == null) {
            fg = surface.createSurface(stableCaps);
        } else if (!surface.checkCompatible(fg, true, true)) {
            Surface s = surface.createSurface(stableCaps);
            s.copy(fg);
            fg.release();
            fg = s;
        }
        if (bg == null) {
            bg = surface.createSurface(stableCaps);
        } else if (!surface.checkCompatible(bg, true, true)) {
            Surface s = surface.createSurface(stableCaps);
            s.copy(bg);
            bg.release();
            bg = s;
        }
    }

    @Override
    public boolean isRenderRequired(Source source, long time) {
        return capturing;
    }
}