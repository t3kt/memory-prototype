package net.t3kt.memory.prototype;

import processing.core.PApplet;
import processing.core.PFont;

import java.text.NumberFormat;

public final class SharedResources {

    private static SharedResources instance;

    public static void setup(PApplet applet) {
        instance = new SharedResources(applet);
    }

    public static SharedResources get() {
        if (instance == null) {
            throw new IllegalStateException("SharedResources hasn't been initialized yet");
        }
        return instance;
    }

    private final NumberFormat floatFormat;
    private final PFont debugFont;

    private SharedResources(PApplet applet) {
        floatFormat = NumberFormat.getIntegerInstance();
        floatFormat.setMinimumIntegerDigits(1);
        floatFormat.setMinimumFractionDigits(2);
        floatFormat.setMaximumFractionDigits(2);
        debugFont = applet.createFont("Lucida Sans", 20f);
    }

    public String formatFloat(float value) {
        return floatFormat.format(value);
    }

    public PFont getDebugFont() {
        return debugFont;
    }

}
