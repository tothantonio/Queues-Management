module org.example.pt2025_30228_toth_antonio_roberto_assignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens application to javafx.fxml;
    opens controller to javafx.fxml;
    opens model to javafx.fxml;
    opens business_logic to javafx.fxml;
    opens util to javafx.fxml;

    exports application;
    exports controller;
    exports model;
    exports business_logic;
    exports util;
}