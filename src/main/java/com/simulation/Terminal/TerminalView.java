package com.simulation.Terminal;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TerminalView {
    private static TerminalView instance;

    private final AnchorPane anchorPane = new AnchorPane();
    private final TextArea terminalArea = new TextArea();
    private final TextField inputTextField = new TextField();
    private final Stage stage = new Stage();

    public static synchronized TerminalView getInstance() {
        if (instance == null) {
            instance = new TerminalView();
        }
        return instance;
    }

    public void terminalInit() {
        stage.initModality(Modality.NONE);
        stage.setTitle("Terminal");
        stage.setResizable(false);

        terminalArea.setPrefWidth(505);
        terminalArea.setPrefHeight(300);
        terminalArea.setFont(Font.font("Courier New", 14));
        terminalArea.setLayoutX(10);
        terminalArea.setEditable(false);
        terminalArea.setFocusTraversable(false);

        inputTextField.setPrefHeight(30);
        inputTextField.setPrefWidth(505);
        inputTextField.setFont(Font.font("Courier New", 14));
        inputTextField.setLayoutX(10);
        inputTextField.setLayoutY(320);
        inputTextField.setPromptText("help");
        inputTextField.setStyle("-fx-prompt-text-fill: rgba(0,0,0,0.58)");


        anchorPane.getChildren().addAll(terminalArea, inputTextField);

        Scene scene = new Scene(anchorPane, 525, 370);
        stage.setScene(scene);
    }

    public TextField getInputTextField() {
        return inputTextField;
    }

    public TextArea getTerminalArea() {
        return terminalArea;
    }

    public Stage getStage() {
        return stage;
    }

}
