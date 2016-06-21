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
@SuppressWarnings({"Convert2streamapi", "FieldCanBeLocal"})
public class GraphApplet extends PApplet {
    private enum SpawnMode {
        RANDOM,
        POLYNET
    }
    private enum MouseMode {
        NONE,
        ATTRACT,
        REPULSE
    }

    private static final float mouseMass = 30;
    private static final int vel = 5;

    private boolean renderTrail = true;
    private boolean renderArcs = true;
    private boolean renderBalls = true;

    private SpawnMode spawnMode = SpawnMode.RANDOM;
    private MouseMode mouseMode = MouseMode.REPULSE;
    private List<GraphNode> nodes;
    private List<GraphArc> arcs;
    private float k;
    private int t;
    private float tMass;
    private int curn;
    private int maybe_desiredNodeCount;
    private float curMass;

    @Override
    public void keyPressed() {
        if (key == '+') {
            maybe_desiredNodeCount++;
        } else if (key == 't') {
            renderTrail = !renderTrail;
        } else if (key == 'b') {
            renderBalls = !renderBalls;
        } else if (key == 'm') {
            switch (mouseMode) {
                case NONE:
                    mouseMode = MouseMode.ATTRACT;
                    break;
                case ATTRACT:
                    mouseMode = MouseMode.REPULSE;
                    break;
                case REPULSE:
                    mouseMode = MouseMode.NONE;
                    break;
            }
        } else if (key == ENTER) {
            if (spawnMode == SpawnMode.RANDOM)
                spawnMode = SpawnMode.POLYNET;
            else
                spawnMode = SpawnMode.RANDOM;
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
        switch (spawnMode) {
            case RANDOM:
                maybe_desiredNodeCount = 15;
                k = sqrt(min(width, height) / maybe_desiredNodeCount) * .05f;
                nodes.add(
                        new GraphNode(
                                random(width / 2 - width / 8, width / 2 + width / 8),
                                random(height / 2 - height / 8, height / 2 + height / 8),
                                4));
                break;
            case POLYNET:
                maybe_desiredNodeCount = 4;
                k = sqrt(width * height / maybe_desiredNodeCount) * .5f;
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

    private void spawnNewNode() {
        int nodeIndexA = (int) (random(1, nodes.size() - 1)) - 1;
        int nodeIndexB = 0;
        boolean gen = false;
        if (random(1)<.1)
            gen=true;
        if (nodes.size() > 5 && gen) {
            nodeIndexB = (int) (random(1, nodes.size() - 1)) - 1;
            while (nodeIndexA == nodeIndexB)
                nodeIndexB = (int) (random(1, nodes.size() - 1)) - 1;
        }
        GraphNode nodeA = nodes.get(nodeIndexA);
        GraphNode nodeB = nodes.get(nodeIndexB);
        GraphNode newNode;
        switch (spawnMode) {
            case RANDOM:
                newNode = new GraphNode(
                    nodeA.position.x + random(nodeA.mass, nodeA.mass + 10),
                    nodeA.position.y + random(nodeA.mass, nodeA.mass + 10),
                    4);
                nodes.add(newNode);
                arcs.add(new GraphArc(newNode, nodeA));
                newNode.incrMass(2);
                nodeA.incrMass(2);
                if (nodes.size() > 5 && gen) {
                    arcs.add(new GraphArc(newNode, nodeB));
                    newNode.incrMass(2);
                    nodeB.incrMass(2);
                }
                break;
            case POLYNET:
                newNode = new GraphNode(random(width), random(height), 10);
                nodes.add(newNode);
                for (GraphNode m : nodes) {
                    if (newNode != m) {
                        arcs.add(new GraphArc(newNode, m));
                    }
                }
                break;
        }
    }

    private void update() {
        if ((t++ % vel) == 0 && curn < maybe_desiredNodeCount) {
            curn++;
            spawnNewNode();
        }
        if (tMass < 1) {
            tMass += .1;
            curMass = sin(PI * tMass) * 600 * (1 - tMass);
        }

        curMass = max(curMass, mouseMass);

        for (GraphNode u : nodes) {
            for (GraphNode v : nodes) {
                if (u != v) {
                    PVector delta = PVector.sub(v.position, u.position);
                    if (delta.mag() != 0) {
                        v.disp.add(delta.copy().normalize().mult(fr(v.mass, u.mass, delta.mag())));
                    }
                }
            }
        }

        for (GraphArc e : arcs) {
            PVector delta = PVector.sub(e.v.position, e.u.position);
            if (delta.mag() != 0) {
                e.v.disp.sub(delta.copy().normalize().mult(fa(e.v.mass, e.u.mass, delta.mag())));
                e.u.disp.add(delta.copy().normalize().mult(fa(e.v.mass, e.u.mass, delta.mag())));
            }
        }

        applyMouseForces();

        for (GraphNode node : nodes) {
            node.update();
            node.constrain(ValueRange.create(new PVector(0, 0), new PVector(width, height)));
            node.setBall(renderBalls);
            node.setTrail(renderTrail);
        }
    }

    private void applyMouseForces() {
        for (GraphNode u : nodes) {
            switch (mouseMode) {
                case ATTRACT: {
                    PVector mousepos = new PVector(mouseX, mouseY);
                    PVector delta = PVector.sub(u.position, mousepos);
                    if (delta.mag() != 0) {
                        u.disp.sub(delta.copy().normalize().mult(fa(u.mass, curMass, delta.mag())));
                    }
                    break;
                }
                case REPULSE: {
                    PVector mousepos = new PVector(mouseX, mouseY);
                    PVector delta = PVector.sub(u.position, mousepos);
                    if (delta.mag() < curMass + u.mass + 100) {
                        u.disp.add(delta.copy().normalize().mult(fr(u.mass, curMass, delta.mag())));
                    }
                    break;
                }
            }
        }
    }

    private void drawMouseConnectors() {
        pushStyle();
        stroke(0, 0, 0, 0.078431375f);
        PVector mousepos = new PVector(mouseX, mouseY);
        for (GraphNode node : nodes) {
            PVector delta = PVector.sub(node.position, mousepos);
            if (delta.mag() > 0) {
                line(node.position.x, node.position.y, mouseX, mouseY);
            }
        }
        popStyle();
    }

    @Override
    public void draw() {
        background(0.9411765f);

        update();

        if (mouseMode == MouseMode.ATTRACT) {
            drawMouseConnectors();
        }

        if (renderArcs) {
            pushStyle();
            noStroke();
            Drawable.drawAll(g, arcs);
            popStyle();
        }

        pushStyle();
        noStroke();
        Drawable.drawAll(g, nodes);
        popStyle();

        pushStyle();
        noFill();
        stroke(0.78431374f, 0.39215687f, 0f, 0.078431375f);
        ellipse(mouseX, mouseY, curMass, curMass);
        popStyle();

    }

    public static void main(String[] args) {
        runSketch(new String[]{GraphApplet.class.getName()}, new GraphApplet());
    }
}
