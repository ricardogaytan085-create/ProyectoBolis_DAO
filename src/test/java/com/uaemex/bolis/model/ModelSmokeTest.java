package com.uaemex.bolis.model;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
//Edicion Ricardo
public class ModelSmokeTest {
    @Test
    void initVentaYReportes() throws Exception {
        System.setProperty("bolis.db", "jdbc:derby:memory:bolisTest" + System.nanoTime() + ";create=true");
        DatabaseInitializer.init();
        Usuario u = new UsuarioDAO().login("admin", "admin").orElseThrow();
        Boli b = new BoliDAO().listar().get(0);
        new AsistenciaDAO().entrada(u.id());
        new AsistenciaDAO().salida(u.id());
        new VentaDAO().registrar(b.id(), u.id(), 1);
        Path dir = Files.createTempDirectory("bolis");
        ReportExporter r = new ReportExporter();
        assertTrue(Files.size(r.ventasCsv(dir.resolve("ventas.csv"))) > 0);
        assertTrue(Files.size(r.asistenciasTxt(dir.resolve("asistencias.txt"))) > 0);
    }
}
