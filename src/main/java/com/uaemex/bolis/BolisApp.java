package com.uaemex.bolis;

import com.uaemex.bolis.controller.AppController;
import javafx.application.Application;
import javafx.stage.Stage;
//Edición Ricardo
public class BolisApp extends Application {
    public void start(Stage stage) {
        new AppController().start(stage);
    }
}
