package com.uaemex.bolis.view;

import com.uaemex.bolis.model.Boli;
import com.uaemex.bolis.model.Usuario;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class EmpleadoView {
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Scene scene(Usuario u, List<Boli> bolis, String msg, Runnable entrada, Runnable salida, BiConsumer<Integer, Integer> vender, Runnable salir) {
        Label reloj = new Label();
        Label info = new Label(msg);
        TextField boliId = new TextField();
        TextField cantidad = new TextField();
        TextArea lista = new TextArea(bolis.stream().map(b -> b.id() + " | " + b.sabor() + " | $" + b.precio() + " | stock " + b.stock()).collect(Collectors.joining("\n")));
        Button in = new Button("Entrada"), out = new Button("Salida"), sale = new Button("Vender"), logout = new Button("Salir");
        Timeline t = new Timeline(new KeyFrame(Duration.ZERO, e -> reloj.setText(F.format(LocalDateTime.now()))), new KeyFrame(Duration.seconds(1)));
        t.setCycleCount(Animation.INDEFINITE);
        t.play();
        lista.setEditable(false);
        boliId.setPromptText("ID boli");
        cantidad.setPromptText("Cantidad");
        in.setOnAction(e -> entrada.run());
        out.setOnAction(e -> salida.run());
        logout.setOnAction(e -> salir.run());
        sale.setOnAction(e -> venta(boliId, cantidad, vender, info));
        VBox root = new VBox(10, new Label("Empleado: " + u.nombre()), reloj, new HBox(10, in, out, logout), lista, new HBox(10, boliId, cantidad, sale), info);
        root.setPadding(new Insets(15));
        return new Scene(root, 520, 420);
    }

    private static void venta(TextField boliId, TextField cantidad, BiConsumer<Integer, Integer> vender, Label info) {
        try {
            vender.accept(Integer.parseInt(boliId.getText()), Integer.parseInt(cantidad.getText()));
        } catch (NumberFormatException e) {
            info.setText("Datos invalidos");
        }
    }
}
