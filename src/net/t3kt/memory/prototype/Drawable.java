package net.t3kt.memory.prototype;

import processing.core.PGraphics;

public interface Drawable {

    static void drawAll(PGraphics g, Iterable<? extends Drawable> drawables) {
        drawables.forEach(d -> d.draw(g));
    }
    void draw(PGraphics g);
}
