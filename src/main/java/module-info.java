module com.simulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.simulation.MainWindow;
    opens com.simulation.MainWindow to javafx.fxml;
}