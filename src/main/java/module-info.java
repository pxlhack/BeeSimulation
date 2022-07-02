module com.simulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.simulation.MainWindow;
    exports com.simulation.Bee;
    opens com.simulation.MainWindow to javafx.fxml;
}