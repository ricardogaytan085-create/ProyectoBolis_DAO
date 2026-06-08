package com.uaemex.bolis.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.function.BiConsumer;

public class LoginView {
    public static Scene scene(String msg, BiConsumer<String, String> entrar) {
        TextField login = new TextField();
        PasswordField password = new PasswordField();
        Button button = new Button("Ingresar");
        login.setPromptText("Usuario");
        password.setPromptText("Password");
        button.setDefaultButton(true);
        button.setOnAction(e -> entrar.accept(login.getText(), password.getText()));
        VBox root = new VBox(10, new Label("Login"), login, password, button, new Label(msg));
        root.setPadding(new Insets(20));
        return new Scene(root, 300, 230);
    }
}
