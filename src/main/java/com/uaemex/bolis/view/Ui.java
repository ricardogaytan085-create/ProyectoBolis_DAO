package com.uaemex.bolis.view;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public final class Ui {
    private static final String FONT = "-fx-font-family:'Segoe UI',Arial,sans-serif;";
    private static final String CONTROL = "-fx-background-color:white;-fx-background-radius:8;-fx-border-color:#cbd5e1;-fx-border-radius:8;-fx-padding:8 10;-fx-prompt-text-fill:#94a3b8;";
    private static final String BUTTON = "-fx-background-color:#4f46e5;-fx-background-radius:999;-fx-text-fill:white;-fx-font-weight:700;-fx-padding:8 14;-fx-cursor:hand;";

    private Ui() {}

    public static Scene scene(VBox root, int width, int height) {
        root.setSpacing(12);
        root.setStyle(FONT + "-fx-padding:18;-fx-background-color:#f8fafc;-fx-font-size:13px;");
        return new Scene(root, width, height);
    }

    public static Label title(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.web("#111827"));
        label.setStyle(FONT + "-fx-font-size:18px;-fx-font-weight:700;");
        return label;
    }

    public static Label message(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.web("#4b5563"));
        label.setStyle(FONT + "-fx-font-weight:600;");
        return label;
    }

    public static TextField field(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle(CONTROL);
        return field;
    }

    public static PasswordField password() {
        PasswordField field = new PasswordField();
        field.setPromptText("Password");
        field.setStyle(CONTROL);
        return field;
    }

    public static TextField smallField(String prompt) {
        TextField field = field(prompt);
        field.setPrefWidth(86);
        return field;
    }

    public static <T> ListView<T> listView(List<T> items) {
        ListView<T> list = new ListView<>();
        list.getItems().addAll(items);
        list.setPrefHeight(160);
        list.setStyle(CONTROL);
        return list;
    }

    public static Button button(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle(BUTTON);
        button.setOnMouseEntered(e -> button.setStyle(BUTTON + "-fx-background-color:#4338ca;"));
        button.setOnMouseExited(e -> button.setStyle(BUTTON));
        button.setOnAction(e -> action.run());
        return button;
    }

    public static HBox row(Node... nodes) {
        HBox row = new HBox(nodes);
        row.setSpacing(10);
        return row;
    }

    public static HBox tightRow(Node... nodes) {
        HBox row = new HBox(nodes);
        row.setSpacing(6);
        return row;
    }

    public static VBox root(Node... nodes) {
        return new VBox(nodes);
    }

    public static VBox box(String title, Node... nodes) {
        VBox box = new VBox(title(title));
        box.setSpacing(8);
        box.setStyle("-fx-padding:12;-fx-background-color:white;-fx-background-radius:10;-fx-border-color:#e5e7eb;-fx-border-radius:10;");
        box.getChildren().addAll(nodes);
        return box;
    }
}
