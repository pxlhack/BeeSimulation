package com.simulation.Bee;

import com.simulation.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class WorkerBee extends Bee {
    public WorkerBee(UUID id, int[] c) {
        super(id, "src/main/resources/com/simulation/Bee/w_bee.png");
        startCoordinates = c;
        line();
    }

    static {
        lifeTime = 3;
    }

    public static void setLifeTime(int lifeTime) {
        WorkerBee.lifeTime = lifeTime;
    }


    private int coordinatesIndex;


    private List<int[]> moveCoordinates;


    private final int[] startCoordinates;


    private boolean toCorner = true;

    public boolean isToCorner() {
        return toCorner;
    }

    public void directToCorner() {
        this.toCorner = true;
    }

    public void directFromCorner() {
        this.toCorner = false;
    }


    private final int cornerIndex = (int) (Math.random() * 4);


    public int[] getCornerCoordinates() {
        return Constant.getCornerCoordinates(cornerIndex);
    }


    private void line() {
        int cornerX = getCornerCoordinates()[0], cornerY = getCornerCoordinates()[1];
        int startX = startCoordinates[0], startY = startCoordinates[1];
        int speed = 2;
        int N = Math.max(Math.abs(startX - cornerX), Math.abs(startY - cornerY)) * speed;
        double dx = (double) (startX - cornerX) / N;
        double dy = (double) (startY - cornerY) / N;
        ArrayList<int[]> rowList = new ArrayList<>();
        if (cornerX > startX) {
            double x = cornerX, y = cornerY;
            for (int i = 0; i < N; i++) {
                rowList.add(new int[]{(int) Math.ceil(x), (int) Math.ceil(y)});
                x += dx;
                y += dy;
            }
            rowList.add(startCoordinates);
            coordinatesIndex = rowList.toArray().length - 1;
        } else {


            double x = startX, y = startY;

            for (int i = 0; i < N; i++) {
                rowList.add(new int[]{(int) Math.ceil(x), (int) Math.ceil(y)});
                x -= dx;
                y -= dy;
            }
            rowList.add(getCornerCoordinates());
            coordinatesIndex = 0;
        }
        moveCoordinates = new ArrayList<>(rowList);

    }

    public void toPreviousCoordinates() {
        if (this.cornerIndex < 2) {
            this.coordinatesIndex++;
        } else {
            this.coordinatesIndex--;
        }
    }

    public void toNextCoordinates() {
        if (this.cornerIndex < 2) {
            this.coordinatesIndex--;
        } else {
            this.coordinatesIndex++;
        }
    }

    @Override
    public void move() {

        if (isToCorner()) {
            if (Arrays.equals(moveCoordinates.get(this.coordinatesIndex), getCornerCoordinates())) {
                directFromCorner();
                toPreviousCoordinates();
            } else {
                toNextCoordinates();
            }
        } else {
            if (Arrays.equals(moveCoordinates.get(this.coordinatesIndex), startCoordinates)) {
                directToCorner();
                toNextCoordinates();
            } else {
                toPreviousCoordinates();
            }
        }

        int x = moveCoordinates.get(coordinatesIndex)[0];
        int y = moveCoordinates.get(coordinatesIndex)[1];
        getImageView().setLayoutX(x);
        getImageView().setLayoutY(y);
    }
}
