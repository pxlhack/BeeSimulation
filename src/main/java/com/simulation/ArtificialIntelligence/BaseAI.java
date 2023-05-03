package com.simulation.ArtificialIntelligence;

public abstract class BaseAI extends Thread {
    protected volatile int threadSleepTime = 5;
    protected volatile boolean isPaused = false;
    protected final Object pauseLock = new Object();

    @Override
    public void run() {
    }

    public void pauseAI() {
        this.isPaused = true;
    }

    public void resumeAI() {
        synchronized (this.pauseLock) {
            this.isPaused = false;
            this.pauseLock.notifyAll();
        }
    }
}