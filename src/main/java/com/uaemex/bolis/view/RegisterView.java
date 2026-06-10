package com.uaemex.bolis.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.function.Consumer;

// Edicion Oliver
public class RegisterView {
    public record Form(String login, String password, String nombres, String paterno, String materno, String fecha, String email, String telefono) {}

    public static Scene scene(String msg, Consumer<Form> registrar, Runnable cancelar) {
        try {
            FXMLLoader loader = new FXMLLoader(RegisterView.class.getResource("/RegisterView.fxml"));
            Parent root = loader.load();
            
            RegisterViewController controller = loader.getController();
            controller.setCallbacks(msg, registrar, cancelar);
            
            Scene scene = new Scene(root, 380, 440);
            scene.getStylesheets().add(RegisterView.class.getResource("/styles.css").toExternalForm());
            return scene;
        } catch (IOException e) {
            throw new RuntimeException("Error loading RegisterView.fxml", e);
        }
    }
}
