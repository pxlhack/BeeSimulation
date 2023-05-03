package com.simulation.ArtificialIntelligence;

import com.simulation.Bee.Bee;
import com.simulation.Bee.MaleBee;
import com.simulation.Model.Habitat;

public class MaleAI extends BaseAI {
    final Habitat h = Habitat.getHabitat();

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
                    if (bee instanceof MaleBee ) {
                        bee.move();
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

