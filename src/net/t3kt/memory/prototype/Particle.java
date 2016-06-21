package net.t3kt.memory.prototype;

import processing.core.PApplet;
import processing.core.PVector;

import static processing.core.PConstants.TWO_PI;

public class Particle {

    PVector velocity;
    float lifespan = 255;

    PVector position;
    float partSize;

    PVector gravity = new PVector(0f,0.1f);


    Particle(PApplet applet) {
        partSize = applet.random(10,60);

        rebirth(applet, applet.sketchWidth()/2f,applet.sketchHeight()/2f);
        lifespan = applet.random(255);
    }

    void rebirth(PApplet applet, float x, float y) {
        float a = applet.random(TWO_PI);
        float speed = applet.random(0.5f,4f);
        velocity = new PVector((float)Math.cos(a), (float)Math.sin(a)).mult(0.1f);
        velocity.mult(speed);
        lifespan = 255;
        position = new PVector(x, y);
    }

    boolean isDead() {
        if (lifespan < 0) {
            return true;
        } else {
            return false;
        }
    }


    public void update(PApplet applet) {
        lifespan = lifespan - 0.2f;
        velocity.add(gravity);

        if ((position.x > applet.sketchWidth()) || (position.x < 0)) {
            velocity.x = velocity.x * -1;
        }
        if ((position.y > applet.sketchHeight()) || (position.y < 0)) {
            velocity.y = velocity.y * -1;
        }

        position.add(velocity);
    }
}