package net.t3kt.memory.prototype.graph;

import net.t3kt.memory.prototype.Drawable;
import net.t3kt.memory.prototype.ValueRange;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Based on http://www.openprocessing.org/sketch/177
 */
public class GraphApplet extends PApplet {
    private enum GraphMode {
        RANDOM,
        POLYNET
    }

    private static final float mouseMass = 30;
    private static final int vel = 5;

    private boolean renderTrail = true;
    private boolean renderArcs = true;
    private boolean mouseAttract = false;
    private boolean mouseRepulse = true;
    private boolean renderBalls = true;

    private GraphMode mode = GraphMode.RANDOM;
    private List<GraphNode> nodes;
    private List<GraphArc> arcs;
    private float k;
    private int t;
    private float tMass;
    private int curn;
    private int nn;
    private float curMass;

    @Override
    public void keyPressed() {
        if (key == '+') {
            nn++;
            return;
        } else if (key == 't') {
            renderTrail = !renderTrail;
            return;
        } else if (key == 'b') {
            renderBalls = !renderBalls;
            return;
        }

        if (key == ENTER) {
            if (mode == GraphMode.RANDOM)
                mode = GraphMode.POLYNET;
            else
                mode = GraphMode.RANDOM;
            prepare();
        }
    }

    @Override
    public void settings() {
        size(400, 400, JAVA2D);
    }

    @Override
    public void setup() {
        smooth();
//  frameRate(1);
        nodes = new ArrayList<>();
        arcs = new ArrayList<>();
        prepare();
        curMass = mouseMass;
        tMass = 1;
        curn = 0;
        colorMode(RGB, 1.0f);
    }

    @Override
    public void mousePressed() {
        curMass = 0;
        tMass = 0;
        redraw();
    }

    private void prepare() {
        nodes.clear();
        arcs.clear();
        switch (mode) {
            case RANDOM:
                nn = 15;
                k = sqrt(min(width, height) / nn) * .05f;
                nodes.add(
                        new GraphNode(
                                random(width / 2 - width / 8, width / 2 + width / 8),
                                random(height / 2 - height / 8, height / 2 + height / 8),
                                4));
                break;
            case POLYNET:
                nn = 4;
                k = sqrt(width * height / nn) * .5f;
                nodes.add(
                        new GraphNode(
                                random(width / 2 - width / 8, width / 2 + width / 8),
                                random(height / 2 - height / 8, height / 2 + height / 8),
                                10));
                break;
        }
        curn = 0;
    }

    private float fa(float m1, float m2, float z) {
        return .0001f * pow(k - m1 - m2 - z, 2f);
        //return .1*pow(m1*m2,2)/pow(z,2); - commented out in original
    }

    private float fr(float m1, float m2, float z) {
        return .5f * pow(m1 + m2 + k, 2f) / pow(z, 2f);
        //return 20*(m1*m2)/pow(z,2); - commented out in original
    }

    @Override
    public void draw() {
        if ((t++ % vel) == 0 && curn < nn) {
            curn++;
            int r = (int) (random(1, nodes.size() - 1)) - 1;
            int s = 0;
            boolean gen = false;
    if (random(1)<.1)
                gen=true;
            if (nodes.size() > 5 && gen) {
                s = (int) (random(1, nodes.size() - 1)) - 1;
                while (r == s)
                    s = (int) (random(1, nodes.size() - 1)) - 1;
            }
            GraphNode nr = nodes.get(r);
            GraphNode ss = nodes.get(s);
            GraphNode newn;
            switch (mode) {
                case RANDOM:
                    newn = new GraphNode(nr.pos.x + random(nr.mass, nr.mass + 10), nr.pos.y + random(nr.mass, nr.mass + 10), 4);
                    nodes.add(newn);
                    arcs.add(new GraphArc(newn, nr));
                    newn.incrMass(2);
                    nr.incrMass(2);
                    if (nodes.size() > 5 && gen) {
                        arcs.add(new GraphArc(newn, ss));
                        newn.incrMass(2);
                        ss.incrMass(2);
                    }
                    break;
                case POLYNET:
                    float prob = random(1);
                    newn = new GraphNode(random(width), random(height), 10);
                    nodes.add(newn);
                    for (GraphNode m : nodes) {
                        if (newn == m) continue;
                        arcs.add(new GraphArc(newn, m));
                    }
                    break;
            }
        }
        background(0.9411765f);
        if (tMass < 1) {
            tMass += .1;
            curMass = sin(PI * tMass) * 600 * (1 - tMass);
            //
        }

        curMass = max(curMass, mouseMass);


        noStroke();
        for (GraphNode u : nodes) {
            for (GraphNode v : nodes) {
                if (u != v) {
                    PVector delta = PVector.sub(v.pos, u.pos);
                    if (delta.mag() != 0) {
                        v.disp.add(delta.copy().normalize().mult(fr(v.mass, u.mass, delta.mag())));
                    }
                }
            }
        }

        for (GraphArc e : arcs) {
            PVector delta = PVector.sub(e.v.pos, e.u.pos);
            if (delta.mag() != 0) {
                e.v.disp.sub(delta.copy().normalize().mult(fa(e.v.mass, e.u.mass, delta.mag())));
                e.u.disp.add(delta.copy().normalize().mult(fa(e.v.mass, e.u.mass, delta.mag())));
            }
        }
        for (GraphNode u : nodes) {
            if (mouseAttract) {
                PVector mousepos = new PVector(mouseX, mouseY);
                PVector delta = PVector.sub(u.pos, mousepos);
                if (delta.mag() != 0) {
                    u.disp.sub(delta.copy().normalize().mult(fa(u.mass, curMass, delta.mag())));
                    stroke(0, 0, 0, 0.078431375f);
                    line(u.pos.x, u.pos.y, mouseX, mouseY);
                    noStroke();
                }
            }
            if (mouseRepulse) {
                PVector mousepos = new PVector(mouseX, mouseY);
                PVector delta = PVector.sub(u.pos, mousepos);
                if (delta.mag() < curMass + u.mass + 100) {
                    u.disp.add(delta.copy().normalize().mult(fr(u.mass, curMass, delta.mag())));
                }
            }
            u.update();
            u.constrain(ValueRange.create(new PVector(0, 0), new PVector(width, height)));
        }
        if (renderArcs) {
            Drawable.drawAll(g, arcs);
        }
        for (GraphNode u : nodes) {
            if (renderTrail)
                u.setTrail(true);
            else
                u.setTrail(false);
            if (renderBalls)
                u.setBall(true);
            else
                u.setBall(false);
            u.draw(g);
        }
        noFill();
        stroke(0.78431374f, 0.39215687f, 0f, 0.078431375f);
        ellipse(mouseX, mouseY, curMass, curMass);

    }

    public static void main(String[] args) {
        runSketch(new String[]{GraphApplet.class.getName()}, new GraphApplet());
    }
}
