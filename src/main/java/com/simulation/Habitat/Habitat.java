package com.simulation.Habitat;

import com.simulation.Bee.Bee;
import com.simulation.Bee.MaleBee;
import com.simulation.Bee.WorkerBee;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.UUID;

public class Habitat {
    private static Habitat h;

    public static synchronized Habitat getHabitat() {
        if (h == null) h = new Habitat();
        return h;
    }

    private Habitat() {
    }


    private static int maleSpawnTime = 2;
    private static int workerSpawnTime = 2;

    private static int workerSpawnChance = 60;
    private static int maleSpawnChance = 30;

    public int getMaleSpawnTime() {
        return maleSpawnTime;
    }

    public void setMaleSpawnTime(int maleSpawnTime) {
        Habitat.maleSpawnTime = maleSpawnTime;
    }

    public int getWorkerSpawnTime() {
        return workerSpawnTime;
    }

    public void setWorkerSpawnTime(int workerSpawnTime) {
        Habitat.workerSpawnTime = workerSpawnTime;
    }

    public int getWorkerSpawnChance() {
        return workerSpawnChance;
    }

    public int getMaleSpawnChance() {
        return maleSpawnChance;
    }

    public void setSpawnChance(int maleChance, int workerChance) {
        maleSpawnChance = maleChance;
        workerSpawnChance = workerChance;
    }

    private boolean isWorkerSpawn(int time) {
        return (time % workerSpawnTime == 0 && Math.random() * 100 <= workerSpawnChance);
    }

    private boolean isMaleSpawn(int time) {
        int allCount = workerCount + maleCount;
        if (allCount == 0) return true;
        int chance = 100 * maleCount / allCount;
        return (time % maleSpawnTime == 0 && chance < maleSpawnChance);
    }

    private void spawnWorkerBee(int dailyTime) {
        int[] c = getRandomCoordinates();
        UUID id = UUID.randomUUID();
        WorkerBee bee = new WorkerBee(id);
        addToCollections(dailyTime, bee, c);
        workerCount++;
    }

    private void spawnMaleBee(int dailyTime) {
        int[] c = getRandomCoordinates();
        UUID id = UUID.randomUUID();
        MaleBee bee = new MaleBee(id);
        addToCollections(dailyTime, bee, c);
        maleCount++;
    }

    private void addToCollections(int dailyTime, Bee bee, int[] c) {
        ImageView imageView = bee.getImageView();
        imageView.setLayoutX(c[0]);
        imageView.setLayoutY(c[1]);
        imageView.setFitWidth(48);
        imageView.setFitHeight(48);

        beeArray.add(bee);
        imageArray.add(imageView);
        root.getChildren().add(imageView);
        hashSetId.add(bee.getId());
        treeMap.put(bee.getId(), dailyTime);
    }

    public void killBeeFromCollection(int index) {
        Bee bee = beeArray.get(index);
        UUID id = bee.getId();
        if (bee instanceof MaleBee) {
            maleCount--;
        } else {
            workerCount--;
        }
        root.getChildren().remove(imageArray.get(index));
        imageArray.remove(index);
        beeArray.remove(index);
        treeMap.remove(id);
        hashSetId.remove(id);
    }

    public int getIndexFromId(UUID id) {
        int k = 0;
        for (int i = 0; i < beeArray.size(); i++) {
            if (beeArray.get(i).getId() == id) return i;
        }
        return k;
    }

    public void update(int daily_time) {
        if (isWorkerSpawn(daily_time)) spawnWorkerBee(daily_time);
        if (isMaleSpawn(daily_time)) spawnMaleBee(daily_time);
        kill(daily_time);
    }

    private void kill(int daily_time) {
        int indexW = -1, indexM = -1;
        for (UUID key : treeMap.keySet()) {
            int maleDeathTime = treeMap.get(key) + MaleBee.getLifeTime();
            int workerDeathTime = treeMap.get(key) + WorkerBee.getLifeTime();
            int index = getIndexFromId(key);

            if (maleDeathTime <= daily_time && beeArray.get(index) instanceof MaleBee) {
                indexM = index;
            }

            if (workerDeathTime <= daily_time && beeArray.get(index) instanceof WorkerBee) {
                indexW = index;
            }

        }

        if (indexW != -1) {
            killBeeFromCollection(indexW);
            if (indexW < indexM) indexM--;
        }

        if (indexM != -1) killBeeFromCollection(indexM);

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


    private final HashSet<UUID> hashSetId = new HashSet<>(1);

    private final TreeMap<UUID, Integer> treeMap = new TreeMap<>();

    public TreeMap<UUID, Integer> getTreeMap() {
        return treeMap;
    }

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