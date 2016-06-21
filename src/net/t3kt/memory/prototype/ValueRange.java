package net.t3kt.memory.prototype;

import com.google.common.base.Function;
import processing.core.PApplet;
import processing.core.PVector;

public abstract class ValueRange<T> implements Function<Float, T> {
    private final T start;
    private final T end;

    private ValueRange(T start, T end) {
        this.start = start;
        this.end = end;
    }

    public T getStart() {
        return start;
    }
    public T getEnd() {
        return end;
    }

    public abstract T getValue(float fraction);

    @Override
    public T apply(Float amount) {
        return getValue(amount);
    }

    public static ValueRange<Float> create(float start, float end) {
        return new ValueRange<Float>(start, end) {
            @Override
            public Float getValue(float fraction) {
                return PApplet.lerp(start, end, fraction);
            }
        };
    }

    public static ValueRange<Color> create(Color start, Color end) {
        return new ValueRange<Color>(start, end) {
            @Override
            public Color getValue(float fraction) {
                return start.lerpRgba(end, fraction);
            }
        };
    }

    public static ValueRange<PVector> create(PVector start, PVector end) {
        return new ValueRange<PVector>(start, end) {
            @Override
            public PVector getValue(float fraction) {
                return PVector.lerp(start, end, fraction);
            }
        };
    }
}
