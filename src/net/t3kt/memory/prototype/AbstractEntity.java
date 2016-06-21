package net.t3kt.memory.prototype;

import processing.core.PVector;

abstract class AbstractEntity implements Drawable {
    protected PVector position;

    AbstractEntity(PVector position) {
        this.position = position;
    }

    float x() {
        return position.x;
    }

    float y() {
        return position.y;
    }

    PVector getPosition() {
        return position;
    }

    float distanceTo(AbstractEntity other) {
        return position.dist(other.position);
    }
}
