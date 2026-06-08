package com.uaemex.bolis.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentaDAO {
    private static final Logger LOG = Logger.getLogger(VentaDAO.class.getName());

    public void registrar(int boliId, int usuarioId, int cantidad) {
        if (boliId <= 0 || usuarioId <= 0 || cantidad <= 0) throw new IllegalArgumentException("Venta invalida");
        run("No se registro venta", () -> Database.transaction(c -> {
            Boli b = boli(c, boliId);
            int updated = Database.update(c, "UPDATE bolis SET stock=stock-? WHERE id=? AND stock>=?", p -> {
                p.setInt(1, cantidad);
                p.setInt(2, boliId);
                p.setInt(3, cantidad);
            });
            if (updated == 0) throw new IllegalArgumentException("Stock insuficiente");
            Database.update(c, "INSERT INTO ventas(boli_id,usuario_id,cantidad,total_venta,fecha_hora) VALUES(?,?,?,?,?)", p -> {
                p.setInt(1, boliId);
                p.setInt(2, usuarioId);
                p.setInt(3, cantidad);
                p.setDouble(4, b.precio() * cantidad);
                p.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            });
            return null;
        }));
    }

    public List<Venta> listar() {
        return run("No se leyeron ventas", () -> Database.list("SELECT * FROM ventas ORDER BY id", this::venta));
    }

    private Boli boli(Connection c, int id) throws SQLException {
        return Database.find(c, "SELECT * FROM bolis WHERE id=?", p -> p.setInt(1, id), this::boli)
                .orElseThrow(() -> new IllegalArgumentException("Boli no existe"));
    }

    private Boli boli(ResultSet r) throws SQLException {
        return new Boli(r.getInt("id"), r.getString("sabor"), r.getDouble("precio"), r.getInt("stock"));
    }

    private Venta venta(ResultSet r) throws SQLException {
        return new Venta(r.getInt("id"), r.getInt("boli_id"), r.getInt("usuario_id"), r.getInt("cantidad"), r.getDouble("total_venta"), r.getTimestamp("fecha_hora").toLocalDateTime());
    }

    private <T> T run(String error, DbCall<T> call) {
        try {
            return call.run();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, error, e);
            throw new RuntimeException(error, e);
        }
    }

    @FunctionalInterface
    private interface DbCall<T> {
        T run() throws SQLException;
    }
}
