package com.uaemex.bolis.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.function.BiConsumer;

public class LoginView {
    public static Scene scene(String msg, BiConsumer<String, String> entrar, Runnable registrar) {
        TextField login = Ui.field("Usuario");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        Button button = Ui.button("Ingresar", () -> entrar.accept(login.getText(), password.getText()));
        button.setDefaultButton(true);
        return Ui.scene(Ui.root(Ui.title("Login"), login, password,
                Ui.tightRow(button, Ui.button("Registrarse", registrar)), Ui.message(msg)), 300, 230);
    }
}
