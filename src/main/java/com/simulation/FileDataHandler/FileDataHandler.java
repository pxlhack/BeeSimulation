package com.simulation.FileDataHandler;

import com.simulation.Bee.Bee;
import com.simulation.Bee.SerializableBee;
import com.simulation.Bee.WorkerBee;
import com.simulation.Model.Habitat;
import com.simulation.MainWindow.MainWindowController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.UUID;

public class FileDataHandler {
    private final Habitat h = Habitat.getHabitat();

    public FileDataHandler() {
    }


    public int save() {
        int count = 0;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/resources/com/simulation/FileDataHandler"));
        fileChooser.setInitialFileName("bees.dat");
        File file = fileChooser.showSaveDialog(h.getStage());

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {

            int size = h.getBeeArray().size();
            for (int i = 0; i < size; ++i) {
                Bee bee = h.getBeeArray().get(i);
                UUID id = bee.getId();
                int birthTime = h.getTreeMap().get(id);

                SerializableBee serializableBee = new SerializableBee(bee, birthTime);
                oos.writeObject(serializableBee);
            }
            count = size;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return count;
    }

    public int load(int dailyTime) {
        int count = 0;
        MainWindowController.getInstance().stopSim();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/resources/com/simulation/FileDataHandler"));
        File file = fileChooser.showOpenDialog(h.getStage());

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {


            while (true) {
                try {
                    SerializableBee serializableBee = (SerializableBee) ois.readObject();
                    Bee bee = serializableBee.getBee();
                    int[] c = h.getRandomCoordinates();

                    ImageView imageView;

                    if (bee instanceof WorkerBee) {
                        imageView = new ImageView(new Image("C:\\image\\w_bee.png"));
                        h.increaseWorkerCount();
                    } else {
                        imageView = new ImageView(new Image("C:\\image\\m_bee.png"));
                        h.increaseMaleCount();
                    }

                    bee.setImageView(imageView);
                    int birthTime = serializableBee.getBirthTime();
                    if (birthTime > dailyTime) birthTime = dailyTime;
                    h.addToCollections(birthTime, bee, c);
                    count++;

                } catch (EOFException ex) {
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        MainWindowController.getInstance().startSim();
        return count;
    }

}
