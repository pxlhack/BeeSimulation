package com.simulation.MainWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.simulation.Habitat.Habitat;

import java.util.Locale;

public class MainWindow extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Habitat h = Habitat.getHabitat();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-window-view.fxml"));
        h.setRoot(loader.load());
        MainWindowController controller = loader.getController();
        h.setScene(new Scene(h.getRoot()));

        h.getScene().setOnKeyTyped(e -> {
            switch (e.getCharacter().toLowerCase(Locale.ROOT)) {
                case "t", "е" -> controller.tTyped();
                case "b", "и" -> controller.bTyped();
                case "e", "у" -> controller.eTyped();
                default -> System.out.println("Error " + e.getCharacter());
            }
        });

        stage.setOnCloseRequest(we -> System.exit(0));
        stage.setResizable(false);
        stage.setScene(h.getScene());
        stage.setTitle("Bee simulation");
        stage.show();
    }

    public void run() {
        launch();
    }

}
