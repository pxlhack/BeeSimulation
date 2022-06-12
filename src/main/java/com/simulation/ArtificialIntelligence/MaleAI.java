package com.simulation.ArtificialIntelligence;

import com.simulation.Bee.MaleBee;
import com.simulation.Habitat.Habitat;

public class MaleAI extends BaseAI {
    final Habitat h = Habitat.getHabitat();

    @Override
    public void run() {
        while (isRunning) {
            synchronized (pauseLock) {

                if (isPaused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }

                }
            }
            synchronized (h) {
                for (int i = 0; i < h.getBeeArray().size(); i++) {
                    if (h.getBeeArray().get(i) instanceof MaleBee)
                        move(i);
                }
            }
            try {
                sleep(threadSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void move(int i) {
        int d = 1;
        int[] turnArr = {0, 0, 0, 0};

        int x, y;
        synchronized (h) {
            x = (int) h.getBeeArray().get(i).getImageView().getLayoutX();
            y = (int) h.getBeeArray().get(i).getImageView().getLayoutY();
        }

        if (y - d > h.getTop()) turnArr[0] = 1;
        if (x + d < h.getRight()) turnArr[1] = 1;
        if (y + d < h.getBottom()) turnArr[2] = 1;
        if (x - d > h.getLeft()) turnArr[3] = 1;

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
        synchronized (h) {

            h.getBeeArray().get(i).getImageView().setLayoutX(x);
            h.getBeeArray().get(i).getImageView().setLayoutY(y);
        }
    }


}

