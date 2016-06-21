package net.t3kt.memory.prototype.graph;

import net.t3kt.memory.prototype.*;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Based on http://www.openprocessing.org/sketch/177
 */
public class GraphNode implements Drawable {

    private static final int TRAIL_LENGTH = 8;

    private static final RandomValueInRangeSupplier colorValSupplier =
            new RandomValueInRangeSupplier(0.078431375f, 0.84313726f);

    private static Color __RANDOM_COLOR() {
        return Color.createRgba(
                colorValSupplier.get(),
                colorValSupplier.get(),
                colorValSupplier.get(),
                1f);
    }

    PVector position;
    PVector disp;
    LinkedList<PVector> positionTrail;
    float mass;
    float newmass;
    Color mycolor;
    boolean trail;
    boolean ball;

    public GraphNode(float x, float y, float mass) {
        this.position = new PVector(x, y);
        this.disp = new PVector();
        this.mass = mass;
        positionTrail = new LinkedList<>();
        for (int i = 0; i < TRAIL_LENGTH; i++) {
            positionTrail.add(position.copy());
        }
        this.mycolor = __RANDOM_COLOR();
        this.ball = true;
        this.trail = true;
    }

    PVector getSecondTrailPosition() {
        return positionTrail.get(2);
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
        positionTrail.remove();
        positionTrail.add(position.copy());
        position.add(disp);
        disp.set(0, 0);
    }

    @Override
    public void draw(PGraphics g) {
        if (mass < newmass)
            mass += .2;
        if (trail) {
            g.pushStyle();
            int i = 0;
            for (PVector oldpos : positionTrail) {
                float percent = (((float) TRAIL_LENGTH - i) / TRAIL_LENGTH);
                mycolor.withAlpha(percent * 0.9f).applyToFill(g);
                g.ellipse(oldpos.x, oldpos.y, 2 * mass * percent, 2 * mass * percent);
            }

            g.popStyle();
        }
        if (ball) {
            g.pushStyle();
            mycolor.applyToFill(g);
            g.ellipse(position.x, position.y, mass * 2, mass * 2);
            g.fill(0.9411765f, 0.9411765f, 0.9411765f);
            g.ellipse(position.x, position.y, mass * 1.5f, mass * 1.5f);
            mycolor.applyToFill(g);
            g.ellipse(position.x, position.y, mass, mass);
            g.popStyle();
        }
    }

    void constrain(ValueRange<PVector> range) {
        this.position = Util.clamp(this.position, range.getStart(), range.getEnd());
    }

    @Override
    public String toString() {
        return position.toString();
    }
}
