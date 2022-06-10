package com.simulation.MainWindow;

import com.simulation.Habitat.Habitat;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainWindowController implements Initializable {

    private final Habitat h = Habitat.getHabitat();


    private Timer mTimer = new Timer();
    private int dailyTime = 0;
    private boolean isWork;


    public MainWindowController() {
    }


    @FXML
    private Label dailyTimeLabel, infoLabel;


    private void startSpawn() {

        dailyTimeLabel.setText("Time: " + dailyTime);
        infoLabel.setVisible(false);
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    h.update(dailyTime);
                    dailyTimeLabel.setText("Time: " + dailyTime);
                    dailyTime++;
                });
            }
        }, 1000, 1000);
    }

    private void stopSpawn() {
        mTimer.cancel();
        mTimer = new Timer();
        dailyTimeLabel.setText("Time: " + dailyTime);
        endSim();
    }

    private void endSim() {
        String info = "Simulation time: " + dailyTime;
        info += "\nMale count: " + h.getMaleCount();
        info += "\nWorker count: " + h.getWorkerCount();

        int allCount = h.getBeeArray().size();
        for (int i = 0; i < allCount; i++) {
            h.killBeeFromCollection(0);
        }
        dailyTime = 0;
        isWork = !isWork;


        infoLabel.setText(info);
        infoLabel.setVisible(true);
    }


    public void bTyped() {
        startSim();
    }

    public void eTyped() {
        stopSim();
    }

    public void tTyped() {
        showTime();
    }


    private void showTime() {
        dailyTimeLabel.setVisible(!dailyTimeLabel.isVisible());
    }

    private void startSim() {
        if (!isWork) {
            startSpawn();
            isWork = !isWork;
        }
    }

    public void stopSim() {
        if (isWork) {
            stopSpawn();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isWork = false;
        infoLabel.setVisible(false);
    }


}
