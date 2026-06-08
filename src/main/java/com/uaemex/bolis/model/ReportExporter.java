package com.uaemex.bolis.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportExporter {
    private static final Logger LOG = Logger.getLogger(ReportExporter.class.getName());
    private final VentaDAO ventas = new VentaDAO();
    private final AsistenciaDAO asistencias = new AsistenciaDAO();

    public Path ventasCsv(Path path) {
        List<String> lines = new ArrayList<>(List.of("id,boli_id,usuario_id,cantidad,total_venta,fecha_hora"));
        ventas.listar().forEach(v -> lines.add(v.id() + "," + v.boliId() + "," + v.usuarioId() + "," + v.cantidad() + "," + v.totalVenta() + "," + v.fechaHora()));
        return write(path, lines, "No se exportaron ventas");
    }

    public Path asistenciasTxt(Path path) {
        List<String> lines = new ArrayList<>(List.of("id | usuario_id | fecha | entrada | salida"));
        asistencias.listar().forEach(a -> lines.add(a.id() + " | " + a.usuarioId() + " | " + a.fecha() + " | " + a.horaEntrada() + " | " + a.horaSalida()));
        return write(path, lines, "No se exportaron asistencias");
    }

    private Path write(Path path, List<String> lines, String error) {
        if (path == null) throw new IllegalArgumentException("Ruta invalida");
        try {
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            return Files.write(path, lines);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, error, e);
            throw new RuntimeException(error, e);
        }
    }
}
