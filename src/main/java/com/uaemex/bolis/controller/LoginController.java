package com.uaemex.bolis.controller;

import com.uaemex.bolis.model.UsuarioDAO;
import com.uaemex.bolis.view.LoginView;
import javafx.stage.Stage;

public class LoginController {
    private final Stage stage;
    private final AppController app;
    private final UsuarioDAO usuarios = new UsuarioDAO();

    public LoginController(Stage stage, AppController app) {
        this.stage = stage;
        this.app = app;
    }

    public void show(String msg) {
        stage.setScene(LoginView.scene(msg, this::login));
    }

    private void login(String usuario, String password) {
        try {
            usuarios.login(usuario, password).ifPresentOrElse(u -> {
                if ("Administrador".equalsIgnoreCase(u.rol())) app.admin(u, "");
                else app.empleado(u, "");
            }, () -> show("Datos incorrectos"));
        } catch (IllegalArgumentException e) {
            show("Datos incorrectos");
        }
    }
}
