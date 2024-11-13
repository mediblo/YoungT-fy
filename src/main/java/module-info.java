module com.youngtfy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires se.michaelthelin.spotify;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires java.sql;

    opens com.youngtfy.client.common to javafx.fxml;
    opens com.youngtfy.client.controller to javafx.fxml;


    exports com.youngtfy.common;

    exports com.youngtfy.client.common;
    exports com.youngtfy.client.controller;

    exports com.youngtfy.server.DAO;
    exports com.youngtfy.server.common;
}