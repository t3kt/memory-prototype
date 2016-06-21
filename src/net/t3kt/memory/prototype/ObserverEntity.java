package net.t3kt.memory.prototype;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.HashSet;
import java.util.Set;

final class ObserverEntity extends AbstractEntity {

    private final Set<OccurrenceEntity> connectedOccurrences = new HashSet<>();
    private final int startTime;
    private final int totalLifetime;

    ObserverEntity(PVector position, int lifetime) {
        super(position);
        this.startTime = (int) System.currentTimeMillis();
        this.totalLifetime = lifetime;
    }

    void addOccurrence(OccurrenceEntity occurrence) {
        connectedOccurrences.add(occurrence);
    }

    void removeOccurrence(OccurrenceEntity occurrence) {
        connectedOccurrences.remove(occurrence);
    }

    Iterable<OccurrenceEntity> getConnectedOccurrences() {
        return connectedOccurrences;
    }

    float getRemainingLifetimeFraction() {
        int elapsed = (int) System.currentTimeMillis() - startTime;
        if (elapsed >= totalLifetime) {
            return 0.0f;
        }
        return PApplet.map((float) elapsed, 0f, (float) totalLifetime, 1f, 0f);
    }

    @Override
    public void draw(PGraphics g) {
        float alpha = getRemainingLifetimeFraction();

        g.pushMatrix();
        g.pushStyle();

        g.translate(position.x, position.y);

        g.fill(0f, 0f, 0f, alpha);

        g.strokeWeight(1.0f);
        g.stroke(0f, 0f, 0f, alpha + 0.1f);

        g.ellipseMode(PConstants.RADIUS);
        g.ellipse(0, 0, 5, 5);

        g.popStyle();

//        g.pushStyle();
//
//        g.textFont(SharedResources.get().getDebugFont());
//        g.textAlign(PConstants.LEFT, PConstants.CENTER);
//        g.textSize(14f);
//        g.fill(0);
//        g.text("life: " + SharedResources.get().formatFloat(alpha), 20f, 0f);
//
//        g.popStyle();
        g.popMatrix();
    }
}
