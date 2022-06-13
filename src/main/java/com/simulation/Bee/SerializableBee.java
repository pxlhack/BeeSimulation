package com.simulation.Bee;

import java.io.Serializable;

public class SerializableBee implements Serializable {
    private final Bee bee;
    private final int birthTime;

    public Bee getBee() {
        return bee;
    }

    public int getBirthTime() {
        return birthTime;
    }

    public SerializableBee(Bee bee, int birthTime) {
        this.bee = bee;
        this.birthTime = birthTime;
    }
}
