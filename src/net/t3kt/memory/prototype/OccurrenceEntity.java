package net.t3kt.memory.prototype;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.HashSet;
import java.util.Set;

final class OccurrenceEntity extends AbstractEntity {

    private final Set<ObserverEntity> connectedObservers = new HashSet<>();
    private final float radius;
    private float actualRadius;

    OccurrenceEntity(PVector position, float radius) {
        super(position);
        this.radius = radius;
        this.actualRadius = 0;
    }

    boolean inRangeOf(AbstractEntity other) {
        float dist = distanceTo(other);
        return dist < radius;
    }

    void addObserver(ObserverEntity observer) {
        connectedObservers.add(observer);
        float dist = distanceTo(observer);
        if (dist > actualRadius) {
            actualRadius = dist;
        }
    }

    void removeObserver(ObserverEntity observer) {
        connectedObservers.remove(observer);
    }

    int observerCount() {
        return connectedObservers.size();
    }

    Iterable<ObserverEntity> getConnectedObservers() {
        return connectedObservers;
    }

    boolean hasConnectedObservers() {
        return !connectedObservers.isEmpty();
    }

    float getAmountOfObservation() {
        float result = 0f;
        for (ObserverEntity observer : connectedObservers) {
            result += observer.getRemainingLifetimeFraction();
        }
        return result;
    }

    private static final float MAX_OBS_LEVEL = 4f;

    @Override
    public void draw(PGraphics g) {
        float amount = getAmountOfObservation();
        if (amount > MAX_OBS_LEVEL) {
            amount = MAX_OBS_LEVEL;
        }
        float alpha = PApplet.map(amount, 0, MAX_OBS_LEVEL, 0.02f, 0.2f);
        g.pushMatrix();
        g.pushStyle();
        g.fill(1f, 0.5f, 0.25f, alpha);
        g.rectMode(PConstants.CENTER);

        g.translate(position.x, position.y);

        g.rect(0, 0, 5, 5);

        g.popStyle();

        g.pushStyle();
        //g.noFill();
        //g.strokeWeight(1);
        //g.stroke(1f, 0f, 0f);
        g.fill(0.5f, 0.5f, 0.5f, alpha);
        g.noStroke();

        g.ellipseMode(PConstants.RADIUS);
        g.ellipse(0, 0, actualRadius, actualRadius);

        g.popStyle();

        g.pushStyle();

        g.noFill();
        g.strokeWeight(1f);
        g.stroke(0.7f, 0.7f, 0.7f, alpha + 0.1f);
        g.ellipseMode(PConstants.RADIUS);
        g.ellipse(0, 0, radius, radius);

        g.popStyle();

//        g.pushStyle();
//
//        g.textFont(SharedResources.get().getDebugFont());
//        g.textAlign(PConstants.LEFT, PConstants.CENTER);
//        g.textSize(14f);
//        g.fill(0);
//        g.text("# obs: " + connectedObservers.size(), 20f, 0f);
//
//        g.popStyle();
        g.popMatrix();
    }
}
