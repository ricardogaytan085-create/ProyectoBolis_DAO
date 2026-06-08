package com.uaemex.bolis;

import com.uaemex.bolis.controller.AppController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public void start(Stage stage) {
        new AppController().start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
