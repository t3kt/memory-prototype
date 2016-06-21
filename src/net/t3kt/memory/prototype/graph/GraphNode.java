package net.t3kt.memory.prototype.graph;

import net.t3kt.memory.prototype.*;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Based on http://www.openprocessing.org/sketch/177
 */
public class GraphNode implements Drawable {
    private static final RandomValueInRangeSupplier colorValSupplier =
            new RandomValueInRangeSupplier(0.078431375f, 0.84313726f);

    private static Color __RANDOM_COLOR() {
        return Color.createRgba(
                colorValSupplier.get(),
                colorValSupplier.get(),
                colorValSupplier.get(),
                1f);
    }

    PVector pos;
    PVector disp;
    PVector[] oldpos;
    float mass;
    float newmass;
    Color mycolor;
    boolean trail;
    boolean ball;

    public GraphNode(float x, float y, float mass) {
        this.pos = new PVector(x, y);
        this.disp = new PVector();
        this.mass = mass;
        this.oldpos = new PVector[8];
        for (int i = 0; i < oldpos.length; i++) {
            oldpos[i] = pos.copy();
        }
        this.mycolor = __RANDOM_COLOR();
        this.ball = true;
        this.trail = true;
    }

    void incrMass(float amount) {
        newmass = mass + amount;
    }

    public void setTrail(boolean trail) {
        this.trail = trail;
    }

    public void setBall(boolean ball) {
        this.ball = ball;
    }

    public void update() {
        for (int i = oldpos.length - 1; i > 0; i--)
            oldpos[i] = oldpos[i - 1];
        oldpos[0] = pos.copy();
        pos.add(disp);
        disp.set(0, 0);
    }

    @Override
    public void draw(PGraphics g) {
        if (mass < newmass)
            mass += .2;
        if (trail) {
            for (int i = 0; i < oldpos.length; i++) {
                float perc = (((float) oldpos.length - i) / oldpos.length);
                mycolor.withAlpha(perc * 0.9f).applyToFill(g);
                g.ellipse(oldpos[i].x, oldpos[i].y, 2 * mass * perc, 2 * mass * perc);
            }
        }
        if (ball) {
            mycolor.applyToFill(g);
            g.ellipse(pos.x, pos.y, mass * 2, mass * 2);
            g.fill(0.9411765f, 0.9411765f, 0.9411765f);
            g.ellipse(pos.x, pos.y, mass * 1.5f, mass * 1.5f);
            mycolor.applyToFill(g);
            g.ellipse(pos.x, pos.y, mass, mass);
        }
    }

    void constrain(ValueRange<PVector> range) {
        this.pos = Util.clamp(this.pos, range.getStart(), range.getEnd());
    }

    @Override
    public String toString() {
        return pos.toString();
    }
}
