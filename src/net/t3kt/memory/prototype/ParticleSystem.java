package net.t3kt.memory.prototype;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class ParticleSystem {
    List<Particle> particles;

    ParticleSystem(PApplet applet, int n) {
        particles = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Particle p = new Particle(applet);
            p.rebirth(applet, applet.random(0f, applet.sketchWidth()), applet.random(0f, applet.sketchHeight()));
            particles.add(p);
        }
    }

    void update(PApplet applet) {
        for (Particle p : particles) {
            if (p.isDead()) {
                p.rebirth(applet, applet.random(0f, applet.sketchWidth()), applet.random(0f, applet.sketchHeight()));
            }
            p.gravity = new PVector(applet.random(-1f, 1f), applet.random(-1f, 1f)).mult(0.01f);
            p.update(applet);
        }
    }

    void setEmitter(PApplet applet, float x, float y) {
        for (Particle p : particles) {
            if (p.isDead()) {
                p.rebirth(applet, x, y);
            }
        }
    }

    void display(PApplet applet) {

        applet.pushStyle();
        applet.noStroke();
        applet.ellipseMode(PApplet.CENTER);
        for (Particle particle : particles) {
            applet.fill(0.5f, 0.5f, 0.75f, particle.lifespan);
            applet.ellipse(particle.position.x, particle.position.y, 10, 10);
        }
        applet.popStyle();
    }
}
