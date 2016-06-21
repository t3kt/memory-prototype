package net.t3kt.memory.prototype;

import com.google.common.collect.Range;
import processing.core.PApplet;

public class Util {

    public static final Range<Float> ZERO_TO_ONE = Range.open(0f, 1f);

    public static float map(float value, Range<Float> inRange, Range<Float> outRange) {
        return PApplet.map(value, inRange.lowerEndpoint(), inRange.upperEndpoint(), outRange.lowerEndpoint(), outRange.upperEndpoint());
    }

    public static float clamp(float val, float low, float high) {
        if (val < low) {
            return low;
        }
        if (val > high) {
            return high;
        }
        return val;
    }
}
