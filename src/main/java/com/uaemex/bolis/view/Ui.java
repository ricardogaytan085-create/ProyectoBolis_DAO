package com.uaemex.bolis.view;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
//Edición Ricardo
public final class Ui {
    private static final String STYLESHEET = "/styles.css";

    private Ui() {}

    public static Scene scene(VBox root, int width, int height) {
        root.setSpacing(12);
        root.getStyleClass().add("app-root");
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(Ui.class.getResource(STYLESHEET).toExternalForm());
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

    public static Label label(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("field-label");
        return label;
    }

    public static TextField field(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        return field;
    }

    public static PasswordField password() {
        PasswordField field = new PasswordField();
        field.setPromptText("Password");
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
        return list;
    }

    public static Button button(String text, Runnable action) {
        Button button = new Button(text);
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

    public static GridPane formGrid(Node... nodes) {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(6);
        grid.getStyleClass().add("form-grid");
        for (int i = 0; i < nodes.length; i += 4) {
            int row = i / 4;
            grid.add(nodes[i], 0, row);
            grid.add(nodes[i + 1], 1, row);
            if (i + 3 < nodes.length) {
                grid.add(nodes[i + 2], 2, row);
                grid.add(nodes[i + 3], 3, row);
            }
        }
        return grid;
    }

    public static VBox root(Node... nodes) {
        return new VBox(nodes);
    }

    public static VBox box(String title, Node... nodes) {
        VBox box = new VBox(title(title));
        box.setSpacing(8);
        box.getStyleClass().add("card");
        box.getChildren().addAll(nodes);
        return box;
    }
}
