package com.uaemex.bolis.model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VentaDAO {
    private static final Logger LOG = Logger.getLogger(VentaDAO.class.getName());

    public void registrar(int boliId, int usuarioId, int cantidad) {
        validar(boliId, usuarioId, cantidad);
        try (Connection c = Database.connect()) {
            c.setAutoCommit(false);
            Boli b = boli(c, boliId);
            try (PreparedStatement u = c.prepareStatement("UPDATE bolis SET stock=stock-? WHERE id=? AND stock>=?");
                 PreparedStatement v = c.prepareStatement("INSERT INTO ventas(boli_id,usuario_id,cantidad,total_venta,fecha_hora) VALUES(?,?,?,?,?)")) {
                u.setInt(1, cantidad);
                u.setInt(2, boliId);
                u.setInt(3, cantidad);
                if (u.executeUpdate() == 0) throw new IllegalArgumentException("Stock insuficiente");
                v.setInt(1, boliId);
                v.setInt(2, usuarioId);
                v.setInt(3, cantidad);
                v.setDouble(4, b.precio() * cantidad);
                v.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                v.executeUpdate();
                c.commit();
            } catch (RuntimeException | SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se registro venta", e);
            throw new RuntimeException("No se registro venta", e);
        }
    }

    public List<Venta> listar() {
        List<Venta> lista = new ArrayList<>();
        try (Connection c = Database.connect();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery("SELECT * FROM ventas ORDER BY id")) {
            while (r.next()) lista.add(new Venta(r.getInt("id"), r.getInt("boli_id"), r.getInt("usuario_id"), r.getInt("cantidad"), r.getDouble("total_venta"), r.getTimestamp("fecha_hora").toLocalDateTime()));
            return lista;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se leyeron ventas", e);
            throw new RuntimeException("No se leyeron ventas", e);
        }
    }

    private Boli boli(Connection c, int id) throws SQLException {
        try (PreparedStatement p = c.prepareStatement("SELECT * FROM bolis WHERE id=?")) {
            p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                if (!r.next()) throw new IllegalArgumentException("Boli no existe");
                return new Boli(r.getInt("id"), r.getString("sabor"), r.getDouble("precio"), r.getInt("stock"));
            }
        }
    }

    private void validar(int boliId, int usuarioId, int cantidad) {
        if (boliId <= 0 || usuarioId <= 0 || cantidad <= 0) throw new IllegalArgumentException("Venta invalida");
    }
}
