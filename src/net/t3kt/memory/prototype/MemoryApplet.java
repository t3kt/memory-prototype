package net.t3kt.memory.prototype;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MemoryApplet extends PApplet {

    private static final int NUM_OBSERVERS = 20;
    private static final int OCCURRENCE_INTERVAL_MILLIS = 3000;
    private static final int OBSERVER_SPAWN_INTERVAL_MILLIS = 6000;

    private final RandomValueInRangeSupplier occurrenceRadiusRange = new RandomValueInRangeSupplier(50f, 300f);
    private final RandomValueInRangeSupplier observerLifetimeRange = new RandomValueInRangeSupplier(5000f, 60000f);

    private Set<ObserverEntity> observers;
    private Set<OccurrenceEntity> occurrences;
    private Set<Animation> animations;
    private int lastOccurrenceSpawnMillis;
    private int lastObserverSpawnMillis;

    @Override
    public void settings() {
        size(800, 800);
    }

    @Override
    public void setup() {
        SharedResources.setup(this);
        animations = new HashSet<>();
        observers = new HashSet<>();
        occurrences = new HashSet<>();
        for (int i = 0; i < NUM_OBSERVERS; i++) {
            spawnObserver();
        }
        colorMode(RGB, 1f);
    }

    private PVector randomPosition() {
        return new PVector(random(width), random(height));
    }

    private void spawnOccurrence() {
        OccurrenceEntity occurrence = new OccurrenceEntity(
                randomPosition(),
                occurrenceRadiusRange.get());
        boolean connected = false;
        for (ObserverEntity observer : observers) {
            if (occurrence.inRangeOf(observer)) {
                occurrence.addObserver(observer);
                observer.addOccurrence(occurrence);
                connected = true;
            }
        }
        if (connected) {
            occurrences.add(occurrence);
        }
    }

    private void spawnObserver() {
        observers.add(new ObserverEntity(
                randomPosition(),
                observerLifetimeRange.get().intValue()));
    }

    private void update() {

        int now = millis();
        if (now - lastOccurrenceSpawnMillis >= OCCURRENCE_INTERVAL_MILLIS) {
            lastOccurrenceSpawnMillis = now;
            spawnOccurrence();
        }
        if (now - lastObserverSpawnMillis >= OBSERVER_SPAWN_INTERVAL_MILLIS) {
            lastObserverSpawnMillis = now;
            spawnObserver();
        }

        Set<ObserverEntity> killObservers = observers.stream()
                .filter(observer -> observer.getRemainingLifetimeFraction() <= 0f)
                .collect(Collectors.toSet());
        Set<OccurrenceEntity> killOccurrences = new HashSet<>();
        ValueRange<Float> getRadius = ValueRange.create(0f, 50f);
        Optional<Function<Float, Color>> getStrokeColor =
                Optional.of(
                        ValueRange.create(Color.createRgba(0, 0, 0.7f, 1f), Color.createRgba(0, 0, 0.5f, 0f)));
        for (ObserverEntity observer : killObservers) {
            for (OccurrenceEntity occurrence : observer.getConnectedOccurrences()) {
                occurrence.removeObserver(observer);
                if (!occurrence.hasConnectedObservers()) {
                    killOccurrences.add(occurrence);
                    animations.add(
                            Animation.expandingCircle(
                                    2000,
                                    occurrence.getPosition(),
                                    getRadius,
                                    Optional.absent(),
                                    getStrokeColor)
                                    .start(now));
                }
            }
        }
        observers.removeAll(killObservers);
        occurrences.removeAll(killOccurrences);

        Set<Animation> killAnimations = animations.stream()
                .filter(animation -> {
                    animation.update(now);
                    return animation.isDone();
                })
                .collect(Collectors.toSet());
        animations.removeAll(killAnimations);
    }

    @Override
    public void draw() {
        update();

        clear();
        background(1f);

        Drawable.drawAll(g, observers);

//        Drawable.drawAll(occurrences);

        for (OccurrenceEntity occurrence : occurrences) {
            occurrence.draw(g);

            for (ObserverEntity observer : occurrence.getConnectedObservers()) {
                drawConnection(observer, occurrence);
            }
        }

        Drawable.drawAll(g, animations);
    }

    private void drawConnection(ObserverEntity observer, OccurrenceEntity occurrence) {
        pushStyle();
        strokeWeight(1);
        noFill();
        stroke(0.25f, 0.25f, 1.0f, observer.getRemainingLifetimeFraction());

        line(observer.x(), observer.y(), occurrence.x(), occurrence.y());

        popStyle();
    }

    public static void main(String[] args) {
        runSketch(new String[]{MemoryApplet.class.getName()}, new MemoryApplet());
    }
}
