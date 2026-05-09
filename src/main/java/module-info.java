module com.example.mesos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires java.rmi;
    requires jdk.jfr;
    requires com.fasterxml.jackson.annotation;
    requires jdk.xml.dom;
    requires java.sql;

    opens it.polimi.ingsw.networking to javafx.fxml;
    opens it.polimi.ingsw.model to com.fasterxml.jackson.databind, javafx.fxml;
    opens it.polimi.ingsw to javafx.fxml;
    exports it.polimi.ingsw;
    exports it.polimi.ingsw.model.characters.DTO;
    opens it.polimi.ingsw.model.characters.DTO to javafx.fxml, com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.model;
    exports it.polimi.ingsw.model.events.DTO;
    opens it.polimi.ingsw.model.events.DTO to javafx.fxml, com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.model.board;
    opens it.polimi.ingsw.model.board to javafx.fxml, com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.model.effects;
    opens it.polimi.ingsw.model.effects to javafx.fxml, com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.controller;
    opens it.polimi.ingsw.controller to javafx.fxml;
    exports it.polimi.ingsw.controller.actions;
    opens it.polimi.ingsw.controller.actions to javafx.fxml, com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.controller.states;
    opens it.polimi.ingsw.controller.states to javafx.fxml, com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.networking;
    exports it.polimi.ingsw.networking.RMI;
    exports it.polimi.ingsw.networking.TCP;
    exports it.polimi.ingsw.UI.GUI;
    opens it.polimi.ingsw.UI.GUI to javafx.fxml;
    exports it.polimi.ingsw.UI.TUI;
    opens it.polimi.ingsw.UI.TUI to javafx.fxml;
    exports it.polimi.ingsw.UI;
    opens it.polimi.ingsw.UI to javafx.fxml;
}