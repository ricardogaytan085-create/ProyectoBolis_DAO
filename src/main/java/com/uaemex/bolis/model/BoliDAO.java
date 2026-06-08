package com.uaemex.bolis.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoliDAO {
    private static final Logger LOG = Logger.getLogger(BoliDAO.class.getName());

    public void guardar(String sabor, double precio, int stock) {
        validar(sabor, precio, stock);
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("INSERT INTO bolis(sabor,precio,stock) VALUES(?,?,?)")) {
            p.setString(1, sabor);
            p.setDouble(2, precio);
            p.setInt(3, stock);
            p.executeUpdate();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se guardo el boli", e);
            throw new RuntimeException("No se guardo el boli", e);
        }
    }

    public void actualizar(int id, String sabor, double precio, int stock) {
        validarId(id);
        validar(sabor, precio, stock);
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("UPDATE bolis SET sabor=?, precio=?, stock=? WHERE id=?")) {
            p.setString(1, sabor);
            p.setDouble(2, precio);
            p.setInt(3, stock);
            p.setInt(4, id);
            p.executeUpdate();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se actualizo el boli", e);
            throw new RuntimeException("No se actualizo el boli", e);
        }
    }

    public void eliminar(int id) {
        validarId(id);
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("DELETE FROM bolis WHERE id=?")) {
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se elimino el boli", e);
            throw new RuntimeException("No se elimino el boli", e);
        }
    }

    public Optional<Boli> buscar(int id) {
        validarId(id);
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("SELECT * FROM bolis WHERE id=?")) {
            p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? Optional.of(boli(r)) : Optional.empty();
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se leyo el boli", e);
            throw new RuntimeException("No se leyo el boli", e);
        }
    }

    public List<Boli> listar() {
        List<Boli> lista = new ArrayList<>();
        try (Connection c = Database.connect();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery("SELECT * FROM bolis ORDER BY id")) {
            while (r.next()) lista.add(boli(r));
            return lista;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se leyeron bolis", e);
            throw new RuntimeException("No se leyeron bolis", e);
        }
    }

    private Boli boli(ResultSet r) throws SQLException {
        return new Boli(r.getInt("id"), r.getString("sabor"), r.getDouble("precio"), r.getInt("stock"));
    }

    private void validar(String sabor, double precio, int stock) {
        new Boli(0, sabor, precio, stock);
    }

    private void validarId(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id invalido");
    }
}
