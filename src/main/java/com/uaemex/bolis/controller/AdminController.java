package com.uaemex.bolis.controller;

import com.uaemex.bolis.model.*;
import com.uaemex.bolis.view.AdminView;
import javafx.stage.Stage;

import java.nio.file.Path;

public class AdminController {
    private final Stage stage;
    private final Usuario admin;
    private final AppController app;
    private final UsuarioDAO usuarios = new UsuarioDAO();
    private final BoliDAO bolis = new BoliDAO();
    private final ReportExporter reportes = new ReportExporter();

    public AdminController(Stage stage, Usuario admin, AppController app) {
        this.stage = stage;
        this.admin = admin;
        this.app = app;
    }

    public void show(String msg) {
        stage.setScene(AdminView.scene(admin, usuarios.listar(), bolis.listar(), msg,
                f -> run(() -> usuarios.guardar(f.login(), f.password(), f.nombre(), f.rol()), "Usuario guardado"),
                f -> run(() -> usuarios.actualizar(id(f.id()), f.login(), f.password(), f.nombre(), f.rol()), "Usuario actualizado"),
                s -> run(() -> usuarios.eliminar(id(s)), "Usuario eliminado"),
                f -> run(() -> bolis.guardar(f.sabor(), dec(f.precio()), id(f.stock())), "Boli guardado"),
                f -> run(() -> bolis.actualizar(id(f.id()), f.sabor(), dec(f.precio()), id(f.stock())), "Boli actualizado"),
                s -> run(() -> bolis.eliminar(id(s)), "Boli eliminado"),
                () -> run(() -> reportes.ventasCsv(Path.of("reportes", "ventas.csv")), "Ventas exportadas"),
                () -> run(() -> reportes.asistenciasTxt(Path.of("reportes", "asistencias.txt")), "Asistencias exportadas"),
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

    private int id(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Numero invalido");
        }
    }

    private double dec(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Numero invalido");
        }
    }
}
