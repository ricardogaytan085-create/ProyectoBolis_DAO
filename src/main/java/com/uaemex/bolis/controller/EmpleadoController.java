package com.uaemex.bolis.controller;

import com.uaemex.bolis.model.*;
import com.uaemex.bolis.view.EmpleadoView;
import javafx.stage.Stage;

public class EmpleadoController {
    private final Stage stage;
    private final Usuario usuario;
    private final AppController app;
    private final BoliDAO bolis = new BoliDAO();
    private final AsistenciaDAO asistencias = new AsistenciaDAO();
    private final VentaDAO ventas = new VentaDAO();

    public EmpleadoController(Stage stage, Usuario usuario, AppController app) {
        this.stage = stage;
        this.usuario = usuario;
        this.app = app;
    }

    public void show(String msg) {
        stage.setScene(EmpleadoView.scene(usuario, bolis.listar(), msg,
                () -> run(() -> asistencias.entrada(usuario.id()), "Entrada registrada"),
                () -> run(() -> asistencias.salida(usuario.id()), "Salida registrada"),
                (boliId, cantidad) -> run(() -> ventas.registrar(boliId, usuario.id(), cantidad), "Venta registrada"),
                () -> app.login("")));
    }

    private void run(Runnable r, String ok) {
        try {
            r.run();
            show(ok);
        } catch (RuntimeException e) {
            show(e.getMessage());
        }
    }
}
