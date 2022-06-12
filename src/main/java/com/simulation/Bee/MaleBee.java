package com.simulation.Bee;

import java.util.UUID;

public class MaleBee extends Bee {
    public MaleBee(UUID id) {
        super(id, "C:\\image\\m_bee.png");
    }

    private static int lifeTime = 3;

    public static int getLifeTime() {
        return lifeTime;
    }

    public static void setLifeTime(int lifeTime) {
        MaleBee.lifeTime = lifeTime;
    }
}
