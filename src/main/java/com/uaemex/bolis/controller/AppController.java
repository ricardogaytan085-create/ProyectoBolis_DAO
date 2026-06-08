package com.uaemex.bolis.controller;

import com.uaemex.bolis.model.DatabaseInitializer;
import com.uaemex.bolis.model.Usuario;
import javafx.stage.Stage;

public class AppController {
    private Stage stage;

    public void start(Stage stage) {
        this.stage = stage;
        DatabaseInitializer.init();
        login("");
        stage.setTitle("Proyecto Bolis");
        stage.show();
    }

    void login(String msg) {
        new LoginController(stage, this).show(msg);
    }

    void empleado(Usuario usuario, String msg) {
        new EmpleadoController(stage, usuario, this).show(msg);
    }

    void admin(Usuario usuario, String msg) {
        new AdminController(stage, usuario, this).show(msg);
    }
}
