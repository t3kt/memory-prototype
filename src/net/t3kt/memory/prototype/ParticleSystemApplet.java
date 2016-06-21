package net.t3kt.memory.prototype;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class ParticleSystemApplet extends PApplet {

    private ParticleSystem particleSystem;

    private final List<Entity> entities = new ArrayList<>();

    @Override
    public void settings() {
        size(400, 400);
    }

    @Override
    public void setup() {
        particleSystem = new ParticleSystem(this, 40);
//        for (int i = 0; i < 40; i++) {
//            entities.add(new Entity(new PVector(random(0, width), random(0, width))));
//        }
    }

    private void update() {
//        for (Entity entity : entities) {
////            PVector velocity = PVector.fromAngle(noise(entity.id + frameCount/100.0f) * TWO_PI).mult(0.9f);
//            PVector velocity = PVector.fromAngle(random(0, TWO_PI)).mult(1.9f);
////            PVector velocity = new PVector(noise(entity.id + frameCount/100.0f), noise(1000+entity.id + frameCount/100.0f));
//
////            entity.velocity = entity.velocity.lerp(new PVector(random(-5, 5), random(-5, 5)), 0.01f);
//            PVector newPos = PVector.add(entity.position, velocity);
//            if (newPos.x < 0 || newPos.x >= sketchWidth()) {
//                velocity.x *= -1;
//            }
//            if (newPos.y < 0 || newPos.y >= sketchHeight()) {
//                velocity.y *= -1;
//            }
////            newPos = PVector.add(entity.position, velocity);
//            entity.position.lerp(newPos, 0.8f);
//        }
        particleSystem.update(this);
    }

    @Override
    public void draw() {
        update();

        clear();
        background(255);

//        pushStyle();
//        ellipseMode(CENTER);
//        fill(128);
//        for (Entity entity : entities) {
//            ellipse(entity.position.x, entity.position.y, 10, 10);
//        }
//        popStyle();

        particleSystem.display(this);

        pushStyle();
        stroke(0, 0, 0);
        strokeWeight(1.0f);
        noFill();
//        for (int i = 0; i < entities.size(); i++) {
//            Entity entityA = entities.get(i);
//            for (int j = i + 1; j < entities.size(); j++) {
//                Entity entityB = entities.get(j);
//                float dist = entityA.position.dist(entityB.position);
//                if (dist < 80) {
//                    strokeWeight(map(dist, 80, 0, 0, 5));
//                    line(entityA.position.x, entityA.position.y, entityB.position.x, entityB.position.y);
//                }
//            }
//        }
        for (int i = 0; i < particleSystem.particles.size(); i++) {
            Particle entityA = particleSystem.particles.get(i);
            for (int j = i + 1; j < particleSystem.particles.size(); j++) {
                Particle entityB = particleSystem.particles.get(j);
                float dist = entityA.position.dist(entityB.position);
                if (dist < 80) {
                    strokeWeight(map(dist, 80, 0, 0, 5));
                    line(entityA.position.x, entityA.position.y, entityB.position.x, entityB.position.y);
                }
            }
        }
        popStyle();
    }

    public static void main(String[] args) {
        runSketch(new String[]{ParticleSystemApplet.class.getName()}, new ParticleSystemApplet());
    }
}
