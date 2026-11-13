module vallegrande.edu.pe.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens vallegrande.edu.pe.demo to javafx.fxml;
    opens vallegrande.edu.pe.demo.controller to javafx.fxml;
    opens vallegrande.edu.pe.demo.model to javafx.base;

    exports vallegrande.edu.pe.demo;
    exports vallegrande.edu.pe.demo.controller;
    exports vallegrande.edu.pe.demo.model;
}
