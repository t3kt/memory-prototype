package net.t3kt.memory.prototype;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.Iterator;

public abstract class Animation implements Drawable {

    private final int duration;
    private int startTime = -1;
    protected float fraction = 0f;

    protected Animation(int duration) {
        this.duration = duration;
    }

    public boolean isStarted() {
        return startTime != -1;
    }

    public Animation start(int nowTime) {
        startTime = nowTime;
        fraction = 0f;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public void update(int nowTime) {
        if (!isStarted()) {
            throw new IllegalStateException("Animation hasn't been started");
        }
        fraction = PApplet.map(nowTime - startTime, 0, duration, 0, 1);
        if (fraction > 1) {
            fraction = 1f;
        }
    }

    public boolean isDone() {
        return fraction >= 1f;
    }

    @Override
    public abstract void draw(PGraphics g);

    private static abstract class PositionedAnimation extends Animation {
        private final float x;
        private final float y;

        protected PositionedAnimation(int duration, PVector position) {
            super(duration);
            this.x = position.x;
            this.y = position.y;
        }

        @Override
        public void draw(PGraphics g) {
            g.pushMatrix();
            g.translate(x, y);
            drawCentered(g);
            g.popMatrix();
        }

        protected abstract void drawCentered(PGraphics g);
    }

    public static Animation expandingCircle(
            int duration, PVector position,
            Function<Float, Float> getRadius,
            Optional<Function<Float, Color>> getFillColor,
            Optional<Function<Float, Color>> getStrokeColor) {
        return new PositionedAnimation(duration, position) {
            @Override
            protected void drawCentered(PGraphics g) {
                g.pushStyle();
                g.ellipseMode(PConstants.RADIUS);

                if (getFillColor.isPresent()) {
                    getFillColor.get().apply(fraction).applyToFill(g);
                } else {
                    g.noFill();
                }

                if (getStrokeColor.isPresent()) {
                    getStrokeColor.get().apply(fraction).applyToStroke(g);
                } else {
                    g.noStroke();
                }

                float radius = getRadius.apply(this.fraction);
                g.ellipse(0, 0, radius, radius);

                g.popStyle();
            }
        };
    }

    public static Animation chain(Iterable<? extends Animation> anims) {
        ImmutableList<Animation> animations = ImmutableList.copyOf(anims);
        if (animations.isEmpty()) {
            throw new IllegalArgumentException("Empty animation list");
        }
        int totalDuration = 0;
        for (Animation animation : animations) {
            totalDuration += animation.getDuration();
        }
        Iterator<Animation> iterator = animations.iterator();
        Animation current = iterator.next();
        return new Animation(totalDuration) {

            @Override
            public void draw(PGraphics g) {
                throw new RuntimeException("NOT IMPLEMENTED");
            }
        };
    }

}
