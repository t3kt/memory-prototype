package net.t3kt.memory.prototype;

import com.google.common.collect.Range;

import java.util.Random;
import java.util.function.Supplier;

final class RandomValueInRangeSupplier implements Supplier<Float> {
    private final Random random;
    private final Range<Float> range;

    RandomValueInRangeSupplier(float minVal, float maxVal) {
        range = Range.open(minVal, maxVal);
        this.random = new Random();
    }

    @Override
    public Float get() {
        float val = random.nextFloat();
        return Util.map(val, Util.ZERO_TO_ONE, range);
    }
}
