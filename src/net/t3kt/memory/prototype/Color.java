package net.t3kt.memory.prototype;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Color {
    public final float r;
    public final float g;
    public final float b;
    public final float a;

    private Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static Color createRgba(float r, float g, float b, float a) {
        return new Color(r, g, b, a);
    }

    public Color withAlpha(float a) {
        return createRgba(this.r, this.g, this.b, a);
    }

    public Color lerpRgba(Color other, float amount) {
        return createRgba(
                PApplet.lerp(r, other.r, amount),
                PApplet.lerp(g, other.g, amount),
                PApplet.lerp(b, other.b, amount),
                PApplet.lerp(a, other.a, amount));
    }

    public static Color min(Color color1, Color color2) {
        return createRgba(
                Math.min(color1.r, color2.r),
                Math.min(color1.g, color2.g),
                Math.min(color1.b, color2.b),
                Math.min(color1.a, color2.a));
    }

    public static Color max(Color color1, Color color2) {
        return createRgba(
                Math.max(color1.r, color2.r),
                Math.max(color1.g, color2.g),
                Math.max(color1.b, color2.b),
                Math.max(color1.a, color2.a));
    }

    public void applyToFill(PGraphics graphics) {
        graphics.fill(r, g, b, a);
    }

    public void applyToStroke(PGraphics graphics) {
        graphics.stroke(r, g, b, a);
    }
}
