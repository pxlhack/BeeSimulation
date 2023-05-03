package com.simulation.ArtificialIntelligence;

import com.simulation.Bee.Bee;
import com.simulation.Bee.WorkerBee;
import com.simulation.Model.Habitat;

public class WorkerAI extends BaseAI {
    private final Habitat h = Habitat.getHabitat();

    @Override
    public void run() {
        while (true) {
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
                for (Bee bee : h.getBeeArray()) {
                    if (bee instanceof WorkerBee workerBee) {
                        workerBee.move();
                    }
                }
            }
            try {
                sleep(threadSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
