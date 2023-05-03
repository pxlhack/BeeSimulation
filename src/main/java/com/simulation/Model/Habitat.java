package com.simulation.Model;

import com.simulation.ArtificialIntelligence.MaleAI;
import com.simulation.ArtificialIntelligence.WorkerAI;
import com.simulation.Bee.Bee;
import com.simulation.Bee.MaleBee;
import com.simulation.Bee.WorkerBee;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.UUID;

@Data
public class Habitat {
    private static Habitat h;

    public static synchronized Habitat getHabitat() {
        if (h == null) h = new Habitat();
        return h;
    }

    private Habitat() {

    }


    private int maleSpawnTime, workerSpawnTime;
    private int workerSpawnChance, maleSpawnChance;
    private int maleCount = 0, workerCount = 0;


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
        WorkerBee bee = new WorkerBee(id, c);
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

    public void addToCollections(int dailyTime, Bee bee, int[] c) {
        UUID id = bee.getId();
        ImageView imageView = bee.getImageView();
        imageView.setLayoutX(c[0]);
        imageView.setLayoutY(c[1]);

        imageView.setFitWidth(48);
        imageView.setFitHeight(48);

        synchronized (h) {
            beeArray.add(bee);
            imageArray.add(imageView);
            hashSetId.add(id);
            treeMap.put(id, dailyTime);
            root.getChildren().add(imageView);
            scene.setRoot(root);
        }
    }

    public void killBeeFromCollection(int index) {
        Bee bee = beeArray.get(index);
        UUID id = bee.getId();
        if (bee instanceof MaleBee) {
            maleCount--;
        } else {
            workerCount--;
        }
        synchronized (h) {
            treeMap.remove(id);
            hashSetId.remove(id);
            root.getChildren().remove(imageArray.get(index));
            imageArray.remove(index);
            beeArray.remove(index);
        }

    }

    public int getIndexFromId(UUID id) {
        int k = 0;
        for (int i = 0; i < beeArray.size(); i++) {
            if (beeArray.get(i).getId() == id) return i;
        }
        return k;
    }

    public void update(int currentTime) {
        spawn(currentTime);
        kill(currentTime);
    }

    private void spawn(int currentTime) {
        if (isWorkerSpawn(currentTime)) {
            spawnWorkerBee(currentTime);
        }
        if (isMaleSpawn(currentTime)) {
            spawnMaleBee(currentTime);
        }
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


    private final ArrayList<Bee> beeArray = new ArrayList<>(1);

    private final ArrayList<ImageView> imageArray = new ArrayList<>(1);


    private final HashSet<UUID> hashSetId = new HashSet<>(1);

    private final TreeMap<UUID, Integer> treeMap = new TreeMap<>();

    private Scene scene;

    private AnchorPane root;

    private Stage stage;

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




    private static boolean workerThreadRunned = true;
    private static boolean maleThreadRunned = true;
    private static WorkerAI workerAI;
    private static MaleAI maleAI;

    public void pauseMaleAI() {
        maleAI.pauseAI();
    }

    public void resumeMaleAI() {
        maleAI.resumeAI();
    }

    public void pauseWorkerAI() {
        workerAI.pauseAI();
    }

    public void resumeWorkerAI() {
        workerAI.resumeAI();
    }

    public void startAI() {
        workerAI = new WorkerAI();
        workerAI.start();

        maleAI = new MaleAI();
        maleAI.start();
    }

    private int maleThreadPriority = 5;

    private int workerThreadPriority = 5;

    public void setAIPriority(int malePriority, int workerPriority) {
        maleThreadPriority = malePriority;
        maleAI.setPriority(malePriority);

        workerThreadPriority = workerPriority;
        workerAI.setPriority(workerPriority);

    }


    public void startAllMovement() {
        resumeWorkerAI();
        resumeMaleAI();
    }

    public void stopAllMovement() {
        pauseWorkerAI();
        pauseMaleAI();
    }

    public void maleSleep() {
        if (maleThreadRunned) pauseMaleAI();
        else resumeMaleAI();
        maleThreadRunned = !maleThreadRunned;
    }

    public void workerSleep() {
        if (workerThreadRunned) pauseWorkerAI();
        else resumeWorkerAI();
        workerThreadRunned = !workerThreadRunned;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Habitat;
    }

    public void increaseMaleCount() {
        this.maleCount++;
    }

    public void increaseWorkerCount() {
        this.workerCount++;
    }

}