package com.uaemex.bolis.view;

import com.uaemex.bolis.model.Boli;
import com.uaemex.bolis.model.Usuario;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiConsumer;

public class EmpleadoView {
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Scene scene(Usuario u, List<Boli> bolis, String msg, Runnable entrada, Runnable salida, BiConsumer<Integer, Integer> vender, Runnable salir) {
        Label reloj = new Label();
        Label info = Ui.message(msg);
        TextField boliId = Ui.field("ID boli"), cantidad = Ui.field("Cantidad");
        ListView<Boli> lista = Ui.listView(bolis);
        lista.getSelectionModel().selectedItemProperty().addListener((obs, old, b) -> {
            if (b != null) boliId.setText(String.valueOf(b.id()));
        });
        Timeline t = new Timeline(new KeyFrame(Duration.ZERO, e -> reloj.setText(F.format(LocalDateTime.now()))), new KeyFrame(Duration.seconds(1)));
        t.setCycleCount(Animation.INDEFINITE);
        t.play();
        return Ui.scene(Ui.root(Ui.title("Empleado: " + u.nombre()), reloj,
                Ui.row(Ui.button("Entrada", entrada), Ui.button("Salida", salida), Ui.button("Salir", salir)),
                lista, Ui.row(boliId, cantidad, Ui.button("Vender", () -> venta(boliId, cantidad, vender, info))), info), 520, 420);
    }

    private static void venta(TextField boliId, TextField cantidad, BiConsumer<Integer, Integer> vender, Label info) {
        try {
            vender.accept(Integer.parseInt(boliId.getText()), Integer.parseInt(cantidad.getText()));
        } catch (NumberFormatException e) {
            info.setText("Datos invalidos");
        }
    }
}
