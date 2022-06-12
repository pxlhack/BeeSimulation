package com.simulation.MainWindow;

import com.simulation.Habitat.Habitat;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainWindowController implements Initializable {

    @FXML
    private Label dailyTimeLabel;


    @FXML
    private Button startBtn, stopBtn;

    @FXML
    private CheckBox showTimeBtn;

    @FXML
    private RadioButton showInfoBtn, hideInfoBtn;


    @FXML
    private ComboBox<String> maleBeeChanceComboBox, workerBeeChanceComboBox;

    @FXML
    private TextField maleSpawnTimeTextField, workerSpawnTimeTextField;


    @FXML
    private MenuItem startMenuItem, stopMenuItem;

    @FXML
    private CheckMenuItem showTimeMenuItem;

    @FXML
    private RadioMenuItem showInfoMenuItem, hideInfoMenuItem;


    @FXML
    private void startBtnClick() {
        if (!isWork) {
            startSpawn();
            isWork = !isWork;
            switchBtn(isWork);
        }
    }

    @FXML
    private void stopBtnClick() {
        if (isWork) {
            stopSpawn();
        }
    }

    @FXML
    private void showTimeBtnClick() {
        showTime();
    }

    @FXML
    private void infoBtnCLick() {
        showInfoMenuItem.setSelected(showInfoBtn.isSelected());
        hideInfoMenuItem.setSelected(hideInfoBtn.isSelected());
    }


    @FXML
    private void startMenuItemClick() {
        startBtnClick();
    }

    @FXML
    private void stopMenuItemClick() {
        stopBtnClick();
    }

    @FXML
    private void showTimeMenuItemClick() {
        showTimeBtn.fire();
    }

    @FXML
    private void infoMenuItemClick() {
        showInfoBtn.setSelected(showInfoMenuItem.isSelected());
        hideInfoBtn.setSelected(hideInfoMenuItem.isSelected());
    }

    public MainWindowController() {
    }

    private Timer mTimer = new Timer();
    private int dailyTime = 0;
    private boolean isWork = false;

    private final Habitat h = Habitat.getHabitat();


    private void startSpawn() {

        setSpawnTime();
        setSpawnChance();

        dailyTimeLabel.setText("Time: " + dailyTime);
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
        if (showInfoBtn.isSelected()) showInfoAlert();
        else endSim();
    }

    private void endSim() {
        int allCount = h.getBeeArray().size();
        for (int i = 0; i < allCount; i++) {
            h.killBeeFromCollection(0);
        }
        dailyTime = 0;
        isWork = !isWork;
        switchBtn(isWork);

    }


    private void setSpawnTime() {
        try {
            h.setWorkerSpawnTime(Integer.parseInt(workerSpawnTimeTextField.getText()));

            if (h.getWorkerSpawnTime() <= 0) throw new Error();
        } catch (Throwable t) {
            showNanAlert();
            h.setWorkerSpawnTime(2);
            workerSpawnTimeTextField.setText("2");
        }

        try {
            h.setMaleSpawnTime(Integer.parseInt(maleSpawnTimeTextField.getText()));

            if (h.getMaleSpawnTime() <= 0) throw new Error();
        } catch (Throwable t) {
            showNanAlert();
            h.setMaleSpawnTime(2);
            maleSpawnTimeTextField.setText("2");
        }

    }

    private void setSpawnChance() {
        h.setSpawnChance(Integer.parseInt(maleBeeChanceComboBox.getValue()),
                Integer.parseInt(workerBeeChanceComboBox.getValue()));
    }


    private void showTime() {
        showTimeMenuItem.setSelected(showTimeBtn.isSelected());
        dailyTimeLabel.setVisible(!dailyTimeLabel.isVisible());
    }

    private void showNanAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Input error");
        alert.setHeaderText("Not a number!");
        alert.showAndWait();
    }

    private void showInfoAlert() {
        String info = "Simulation time: " + dailyTime;
        info += "\nMale count: " + h.getMaleCount();
        info += "\nWorker count: " + h.getWorkerCount();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Simulation info");
        alert.setHeaderText(info);
        Optional<ButtonType> result = alert.showAndWait();


        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) endSim();
            if (result.get() == ButtonType.CANCEL) startSpawn();
        }
    }


    public void bTyped() {
        startBtnClick();
    }

    public void eTyped() {
        stopBtnClick();
    }

    public void tTyped() {
        showTimeBtn.fire();
    }

    private void switchBtn(boolean flag) {
        startBtn.setDisable(flag);
        stopBtn.setDisable(!flag);

        startMenuItem.setDisable(flag);
        stopMenuItem.setDisable(!flag);

        maleSpawnTimeTextField.setDisable(flag);
        workerSpawnTimeTextField.setDisable(flag);

        maleBeeChanceComboBox.setDisable(flag);
        workerBeeChanceComboBox.setDisable(flag);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final String[] spawnChance = {"0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
        maleBeeChanceComboBox.getItems().addAll(spawnChance);
        maleBeeChanceComboBox.setValue(Integer.toString(h.getMaleSpawnChance()));

        workerBeeChanceComboBox.getItems().addAll(spawnChance);
        workerBeeChanceComboBox.setValue(Integer.toString(h.getWorkerSpawnChance()));

    }
}
