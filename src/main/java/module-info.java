module com.example.mesos {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires java.rmi;
    requires jdk.jfr;
    requires jdk.xml.dom;
    requires java.sql;
    requires net.bytebuddy;

    exports it.polimi.ingsw.model;
    exports it.polimi.ingsw.model.characters.DTO;
    exports it.polimi.ingsw.model.events.DTO;
    exports it.polimi.ingsw.model.board;
    exports it.polimi.ingsw.model.characters;
    exports it.polimi.ingsw.model.events;
    exports it.polimi.ingsw.model.effects;
    exports it.polimi.ingsw.model.exceptions;
    exports it.polimi.ingsw.controller;
    exports it.polimi.ingsw.controller.actions;
    exports it.polimi.ingsw.controller.states;
    exports it.polimi.ingsw.UI;
    exports it.polimi.ingsw.networking;
    exports it.polimi.ingsw.networking.RMI;
    exports it.polimi.ingsw.networking.TCP;
    exports it.polimi.ingsw.UI.TUI;
    exports it.polimi.ingsw.UI.GUI;
    exports  it.polimi.ingsw.networking.DB;

    opens it.polimi.ingsw to javafx.fxml;
    opens it.polimi.ingsw.model to com.fasterxml.jackson.databind, javafx.fxml;
    opens it.polimi.ingsw.model.characters.DTO to javafx.fxml, com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.model.events.DTO to javafx.fxml, com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.model.board to javafx.fxml, com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.model.effects to javafx.fxml, com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.controller to javafx.fxml;
    opens it.polimi.ingsw.controller.actions to javafx.fxml, com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.controller.states to javafx.fxml, com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.UI.GUI to javafx.fxml;
    opens it.polimi.ingsw.UI.TUI to javafx.fxml;
    opens it.polimi.ingsw.UI to javafx.fxml;
    opens it.polimi.ingsw.networking to javafx.fxml;
    opens it.polimi.ingsw.networking.DB to com.fasterxml.jackson.databind, javafx.fxml;
}