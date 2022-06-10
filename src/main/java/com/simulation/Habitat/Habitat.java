package com.simulation.Habitat;

import com.simulation.Bee.Bee;
import com.simulation.Bee.MaleBee;
import com.simulation.Bee.WorkerBee;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


import java.util.ArrayList;

public class Habitat {
    private static Habitat h;

    public static synchronized Habitat getHabitat() {
        if (h == null) h = new Habitat();
        return h;
    }

    private Habitat() {
    }


    private static final int maleSpawnTime = 2;
    private static final int workerSpawnTime = 2;

    private static final int workerSpawnChance = 60;
    private static final int maleSpawnChance = 30;

    private boolean isWorkerSpawn(int time) {
        return (time % workerSpawnTime == 0 && Math.random() * 100 <= workerSpawnChance);
    }

    private boolean isMaleSpawn(int time) {
        int allCount = workerCount + maleCount;
        if (allCount == 0) return true;
        int chance = 100 * maleCount / allCount;
        return (time % maleSpawnTime == 0 && chance < maleSpawnChance);
    }

    private void spawnWorkerBee() {
        int[] c = getRandomCoordinates();
        WorkerBee bee = new WorkerBee();
        addToCollections(bee, c);
        workerCount++;
    }

    private void spawnMaleBee() {
        int[] c = getRandomCoordinates();
        MaleBee bee = new MaleBee();
        addToCollections(bee, c);
        maleCount++;
    }

    private void addToCollections(Bee bee, int[] c) {
        ImageView imageView = bee.getImageView();
        imageView.setLayoutX(c[0]);
        imageView.setLayoutY(c[1]);
        imageView.setFitWidth(48);
        imageView.setFitHeight(48);

        beeArray.add(bee);
        imageArray.add(imageView);
        root.getChildren().add(imageView);
    }

    public void killBeeFromCollection(int index) {
        Bee bee = beeArray.get(index);
        if (bee instanceof MaleBee) {
            maleCount--;
        } else {
            workerCount--;
        }
        root.getChildren().remove(imageArray.get(index));
        imageArray.remove(index);
        beeArray.remove(index);
    }

    public void update(int daily_time) {
        if (isWorkerSpawn(daily_time)) spawnWorkerBee();
        if (isMaleSpawn(daily_time)) spawnMaleBee();
    }

    private int maleCount = 0;

    public int getMaleCount() {
        return maleCount;
    }

    private int workerCount = 0;

    public int getWorkerCount() {
        return workerCount;
    }


    private final ArrayList<Bee> beeArray = new ArrayList<>(1);

    public ArrayList<Bee> getBeeArray() {
        return beeArray;
    }

    private final ArrayList<ImageView> imageArray = new ArrayList<>(1);


    private Scene scene;

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }


    private AnchorPane root;

    public AnchorPane getRoot() {
        return root;
    }

    public void setRoot(AnchorPane root) {
        this.root = root;
    }


    public static int getBottom() {
        return 220;
    }

    public static int getTop() {
        return 50;
    }

    public static int getLeft() {
        return 50;
    }

    public static int getRight() {
        return 330;
    }

    public int[] getRandomCoordinates() {
        int maxWidth = getRight(), minWidth = getLeft(), maxHeight = getBottom(), minHeight = getTop();

        int x = (int) ((Math.random() * maxWidth) + minWidth);
        int y = (int) ((Math.random() * maxHeight) + minHeight);
        return new int[]{x, y};
    }
}