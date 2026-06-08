package com.uaemex.bolis.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class Ui {
    private static final String CSS = "/com/uaemex/bolis/app.css";

    private Ui() {}

    public static Scene scene(VBox root, int width, int height) {
        root.getStyleClass().add("root-pane");
        Scene scene = new Scene(root, width, height);
        if (Ui.class.getResource(CSS) != null) scene.getStylesheets().add(Ui.class.getResource(CSS).toExternalForm());
        return scene;
    }

    public static Label title(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("title");
        return label;
    }

    public static Label message(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("message");
        return label;
    }

    public static TextField field(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        return field;
    }

    public static TextField smallField(String prompt) {
        TextField field = field(prompt);
        field.getStyleClass().add("small-field");
        return field;
    }

    public static TextArea area(String text) {
        TextArea area = new TextArea(text);
        area.setEditable(false);
        area.setPrefRowCount(10);
        return area;
    }

    public static Button button(String text, Runnable action) {
        Button button = new Button(text);
        button.setOnAction(e -> action.run());
        return button;
    }

    public static HBox row(Node... nodes) {
        HBox row = new HBox(nodes);
        row.getStyleClass().add("row");
        return row;
    }

    public static HBox tightRow(Node... nodes) {
        HBox row = new HBox(nodes);
        row.getStyleClass().add("tight-row");
        return row;
    }

    public static VBox root(Node... nodes) {
        return new VBox(nodes);
    }

    public static VBox box(String title, Node... nodes) {
        VBox box = new VBox(title(title));
        box.getStyleClass().add("section");
        box.getChildren().addAll(nodes);
        return box;
    }
}
