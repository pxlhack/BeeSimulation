package com.simulation.Bee;

import com.simulation.Habitat.Habitat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkerBee extends Bee {
    public WorkerBee(UUID id, int[] c) {
        super(id, "C:\\image\\w_bee.png");
        startCoordinates = c;
        line();
    }

    private static int lifeTime = 3;

    public static int getLifeTime() {
        return lifeTime;
    }

    public static void setLifeTime(int lifeTime) {
        WorkerBee.lifeTime = lifeTime;
    }


    private int dailyIndex;

    public int getDailyIndex() {
        return dailyIndex;
    }

    public void setDailyIndex(int dailyIndex) {
        this.dailyIndex = dailyIndex;
    }


    private List<int[]> moveCoordinates;

    public List<int[]> getMoveCoordinates() {
        return moveCoordinates;
    }


    private final int[] startCoordinates;

    public int[] getStartCoordinates() {
        return startCoordinates;
    }


    private boolean toAngle = true;

    public boolean isToAngle() {
        return toAngle;
    }

    public void setToAngle(boolean toAngle) {
        this.toAngle = toAngle;
    }


    private final int angleNumber = (int) (Math.random() * 4);

    public int getAngleNumber() {
        return angleNumber;
    }


    private final int[] angleCoordinates = Habitat.getRandomAngle(angleNumber);

    public int[] getAngleCoordinates() {
        return angleCoordinates;
    }


    private void line() {
        int angleX = angleCoordinates[0], angleY = angleCoordinates[1];
        int startX = startCoordinates[0], startY = startCoordinates[1];
        int speed = 2;
        int N = Math.max(Math.abs(startX - angleX), Math.abs(startY - angleY)) * speed;
        double dx = (double) (startX - angleX) / N;
        double dy = (double) (startY - angleY) / N;
        ArrayList<int[]> rowList = new ArrayList<>();
        if (angleX > startX) {
            double x = angleX, y = angleY;
            for (int i = 0; i < N; i++) {
                rowList.add(new int[]{(int) Math.ceil(x), (int) Math.ceil(y)});
                x += dx;
                y += dy;
            }
            rowList.add(startCoordinates);
            dailyIndex = rowList.toArray().length - 1;
        } else {


            double x = startX, y = startY;

            for (int i = 0; i < N; i++) {
                rowList.add(new int[]{(int) Math.ceil(x), (int) Math.ceil(y)});
                x -= dx;
                y -= dy;
            }
            rowList.add(angleCoordinates);
            dailyIndex = 0;
        }
        moveCoordinates = new ArrayList<>(rowList);

    }
}
