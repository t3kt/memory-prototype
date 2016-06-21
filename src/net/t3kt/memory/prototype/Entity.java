package net.t3kt.memory.prototype;

import processing.core.PVector;

import java.util.concurrent.atomic.AtomicInteger;

public class Entity {

    private static AtomicInteger nextId = new AtomicInteger(0);

    final int id;
    PVector position;
    float angle;

    Entity(PVector position) {
        this.id = nextId.incrementAndGet();
        this.position = position;
    }
}
