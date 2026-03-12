module com.example.mesos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens it.polimi.ingsw to javafx.fxml;
    exports it.polimi.ingsw;
}