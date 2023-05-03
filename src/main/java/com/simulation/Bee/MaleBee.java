package com.simulation.Bee;

import com.simulation.Constant;

import java.util.UUID;

public class MaleBee extends Bee {
    public MaleBee(UUID id) {

        super(id, "src/main/resources/com/simulation/Bee/m_bee.png");
    }


    public static void setLifeTime(int lifeTime) {
        MaleBee.lifeTime = lifeTime;
    }

    @Override
    public void move() {

        int d = 1;
        int[] turnArr = {0, 0, 0, 0};

        int x = (int) getImageView().getLayoutX();
        int y = (int) getImageView().getLayoutY();


        if (y - d > Constant.getTop()) turnArr[0] = 1;
        if (x + d < Constant.getRight()) turnArr[1] = 1;
        if (y + d < Constant.getBottom()) turnArr[2] = 1;
        if (x - d > Constant.getLeft()) turnArr[3] = 1;

        boolean flag = true;
        int turnDir = 0;
        while (flag) {
            turnDir = (int) (Math.random() * 4);
            if (turnArr[turnDir] == 1) flag = false;
        }

        double r = Math.random();
        if (r > 0.3) {
            switch (turnDir) {
                case 0 -> y -= d;
                case 1 -> x += d;
                case 2 -> y += d;
                case 3 -> x -= d;
            }
        }
        getImageView().setLayoutX(x);
        getImageView().setLayoutY(y);

    }
}
