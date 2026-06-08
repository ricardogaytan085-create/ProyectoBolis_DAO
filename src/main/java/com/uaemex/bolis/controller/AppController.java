package com.uaemex.bolis.controller;

import com.uaemex.bolis.model.DatabaseInitializer;
import com.uaemex.bolis.model.AsistenciaDAO;
import com.uaemex.bolis.model.BoliDAO;
import com.uaemex.bolis.model.ReportExporter;
import com.uaemex.bolis.model.Usuario;
import com.uaemex.bolis.model.UsuarioDAO;
import com.uaemex.bolis.model.VentaDAO;
import com.uaemex.bolis.view.AdminView;
import com.uaemex.bolis.view.EmpleadoView;
import com.uaemex.bolis.view.LoginView;
import javafx.stage.Stage;

import java.nio.file.Path;

public class AppController {
    private Stage stage;
    private Usuario usuario;
    private final UsuarioDAO usuarios = new UsuarioDAO();
    private final BoliDAO bolis = new BoliDAO();
    private final AsistenciaDAO asistencias = new AsistenciaDAO();
    private final VentaDAO ventas = new VentaDAO();
    private final ReportExporter reportes = new ReportExporter();

    public void start(Stage stage) {
        this.stage = stage;
        DatabaseInitializer.init();
        login("");
        stage.setTitle("Proyecto Bolis");
        stage.show();
    }

    private void login(String msg) {
        stage.setScene(LoginView.scene(msg, this::autenticar));
    }

    private void empleado(String msg) {
        stage.setScene(EmpleadoView.scene(usuario, bolis.listar(), msg,
                () -> run(() -> asistencias.entrada(usuario.id()), "Entrada registrada", this::empleado),
                () -> run(() -> asistencias.salida(usuario.id()), "Salida registrada", this::empleado),
                (boliId, cantidad) -> run(() -> ventas.registrar(boliId, usuario.id(), cantidad), "Venta registrada", this::empleado),
                () -> login("")));
    }

    private void admin(String msg) {
        stage.setScene(AdminView.scene(usuario, usuarios.listar(), bolis.listar(), msg,
                f -> run(() -> usuarios.guardar(f.login(), f.password(), f.nombre(), f.rol()), "Usuario guardado", this::admin),
                f -> run(() -> usuarios.actualizar(id(f.id()), f.login(), f.password(), f.nombre(), f.rol()), "Usuario actualizado", this::admin),
                s -> run(() -> usuarios.eliminar(id(s)), "Usuario eliminado", this::admin),
                f -> run(() -> bolis.guardar(f.sabor(), dec(f.precio()), id(f.stock())), "Boli guardado", this::admin),
                f -> run(() -> bolis.actualizar(id(f.id()), f.sabor(), dec(f.precio()), id(f.stock())), "Boli actualizado", this::admin),
                s -> run(() -> bolis.eliminar(id(s)), "Boli eliminado", this::admin),
                () -> run(() -> reportes.ventasCsv(Path.of("reportes", "ventas.csv")), "Ventas exportadas", this::admin),
                () -> run(() -> reportes.asistenciasTxt(Path.of("reportes", "asistencias.txt")), "Asistencias exportadas", this::admin),
                () -> login("")));
    }

    private void autenticar(String login, String password) {
        try {
            usuarios.login(login, password).ifPresentOrElse(u -> {
                usuario = u;
                if ("Administrador".equalsIgnoreCase(u.rol())) admin("");
                else empleado("");
            }, () -> login("Datos incorrectos"));
        } catch (IllegalArgumentException e) {
            login("Datos incorrectos");
        }
    }

    private void run(Runnable action, String ok, java.util.function.Consumer<String> refresh) {
        try {
            action.run();
            refresh.accept(ok);
        } catch (RuntimeException e) {
            refresh.accept(e.getMessage());
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
