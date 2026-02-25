module com.example.mesos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.mesos to javafx.fxml;
    exports com.example.mesos;
}