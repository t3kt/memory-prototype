package net.t3kt.memory.prototype.graph;

import net.t3kt.memory.prototype.Drawable;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Based on http://www.openprocessing.org/sketch/177
 */
public class GraphArc implements Drawable {
    GraphNode v;
    GraphNode u;

    public GraphArc(GraphNode v, GraphNode u) {
        this.v = v;
        this.u = u;
    }


    @Override
    public void draw(PGraphics g) {
        g.pushStyle();
        v.mycolor.lerpRgba(u.mycolor, 0.5f).applyToStroke(g);
        PVector uOldPos = u.getSecondTrailPosition();
        PVector vOldPos = v.getSecondTrailPosition();
        g.bezier(v.position.x, v.position.y, vOldPos.x, vOldPos.y, uOldPos.x, uOldPos.y, u.position.x, u.position.y);
        g.popStyle();
    }
}
