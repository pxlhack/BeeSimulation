package com.simulation.ArtificialIntelligence;

import com.simulation.Bee.WorkerBee;
import com.simulation.Habitat.Habitat;

import java.util.Arrays;
import java.util.List;

public class WorkerAI extends BaseAI {
    Habitat h = Habitat.getHabitat();

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
                    if (h.getBeeArray().get(i) instanceof WorkerBee bee)
                        move(i, bee);
                }
            }
            try {
                sleep(threadSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void move(int i, WorkerBee bee) {
        List<int[]> moveCoord = bee.getMoveCoordinates();
        int[] angle = bee.getAngleCoordinates();
        int angleNumber = bee.getAngleNumber();

        int deltaIndex = 1;

        if (angleNumber < 2) deltaIndex = -1;

        if (bee.isToAngle()) {
            if (Arrays.equals(moveCoord.get(bee.getDailyIndex()), angle)) {
                bee.setToAngle(false);
                bee.setDailyIndex(bee.getDailyIndex() - deltaIndex);

            } else {
                bee.setDailyIndex(bee.getDailyIndex() + deltaIndex);
            }
        } else {
            if (Arrays.equals(moveCoord.get(bee.getDailyIndex()), bee.getStartCoordinates())) {
                bee.setToAngle(true);
                bee.setDailyIndex(bee.getDailyIndex() + deltaIndex);
            } else {
                bee.setDailyIndex(bee.getDailyIndex() - deltaIndex);
            }
        }


        int x = moveCoord.get(bee.getDailyIndex())[0];
        int y = moveCoord.get(bee.getDailyIndex())[1];
        synchronized (h) {
            h.getBeeArray().get(i).getImageView().setLayoutX(x);
            h.getBeeArray().get(i).getImageView().setLayoutY(y);
        }
    }
}
