package com.simulation.Bee;

import java.util.UUID;

public class WorkerBee extends Bee {
    public WorkerBee(UUID id) {
        super(id, "C:\\image\\w_bee.png");
    }

    private static int lifeTime = 3;

    public static int getLifeTime() {
        return lifeTime;
    }

    public static void setLifeTime(int lifeTime) {
        WorkerBee.lifeTime = lifeTime;
    }
}
