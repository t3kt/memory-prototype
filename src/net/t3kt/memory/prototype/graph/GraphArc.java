package net.t3kt.memory.prototype.graph;

import net.t3kt.memory.prototype.Drawable;
import processing.core.PGraphics;

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
//        int r=(int)((red(v.mycolor)+red(u.mycolor))/2);
//        int g=(int)((green(v.mycolor)+green(u.mycolor))/2);
//        int b=(int)((blue(v.mycolor)+blue(u.mycolor))/2);
//        stroke(r,g,b);
        v.mycolor.lerpRgba(u.mycolor, 0.5f).applyToStroke(g);
        //line(v.pos.x,v.pos.y,u.pos.x,u.pos.y); - commented out in original
        g.bezier(v.pos.x,v.pos.y,v.oldpos[2].x,v.oldpos[2].y,u.oldpos[2].x,u.oldpos[2].y,u.pos.x,u.pos.y);
        g.noStroke();
    }
}
