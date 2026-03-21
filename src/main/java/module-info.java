module com.example.mesos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;

    opens it.polimi.ingsw to javafx.fxml;
    exports it.polimi.ingsw;
    exports it.polimi.ingsw.model.characters;
    opens it.polimi.ingsw.model.characters to javafx.fxml;
    exports it.polimi.ingsw.model;
    opens it.polimi.ingsw.model to javafx.fxml;
    exports it.polimi.ingsw.model.events;
    opens it.polimi.ingsw.model.events to javafx.fxml;
    exports it.polimi.ingsw.model.board;
    opens it.polimi.ingsw.model.board to javafx.fxml;
    exports it.polimi.ingsw.model.effects;
    opens it.polimi.ingsw.model.effects to javafx.fxml;
    exports it.polimi.ingsw.model.states;
    opens it.polimi.ingsw.model.states to javafx.fxml;
    exports it.polimi.ingsw.model.actions;
    opens it.polimi.ingsw.model.actions to javafx.fxml;
}