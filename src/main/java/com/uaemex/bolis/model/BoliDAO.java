package com.uaemex.bolis.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoliDAO {
    public void guardar(String sabor, double precio, int stock) {
        validar(sabor, precio, stock);
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("INSERT INTO bolis(sabor,precio,stock) VALUES(?,?,?)")) {
            p.setString(1, sabor);
            p.setDouble(2, precio);
            p.setInt(3, stock);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se guardo el boli", e);
        }
    }

    public void actualizar(int id, String sabor, double precio, int stock) {
        validar(sabor, precio, stock);
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("UPDATE bolis SET sabor=?, precio=?, stock=? WHERE id=?")) {
            p.setString(1, sabor);
            p.setDouble(2, precio);
            p.setInt(3, stock);
            p.setInt(4, id);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se actualizo el boli", e);
        }
    }

    public void eliminar(int id) {
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("DELETE FROM bolis WHERE id=?")) {
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se elimino el boli", e);
        }
    }

    public Optional<Boli> buscar(int id) {
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("SELECT * FROM bolis WHERE id=?")) {
            p.setInt(1, id);
            ResultSet r = p.executeQuery();
            return r.next() ? Optional.of(boli(r)) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("No se leyo el boli", e);
        }
    }

    public List<Boli> listar() {
        List<Boli> lista = new ArrayList<>();
        try (Connection c = Database.connect(); ResultSet r = c.createStatement().executeQuery("SELECT * FROM bolis ORDER BY id")) {
            while (r.next()) lista.add(boli(r));
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("No se leyeron bolis", e);
        }
    }

    private Boli boli(ResultSet r) throws SQLException {
        return new Boli(r.getInt("id"), r.getString("sabor"), r.getDouble("precio"), r.getInt("stock"));
    }

    private void validar(String sabor, double precio, int stock) {
        if (sabor == null || sabor.isBlank() || precio <= 0 || stock < 0) throw new IllegalArgumentException("Datos de boli invalidos");
    }
}
