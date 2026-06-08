package com.uaemex.bolis.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO {
    public void guardar(String login, String password, String nombre, String rol) {
        if (login.isBlank() || password.isBlank() || nombre.isBlank() || rol.isBlank()) throw new IllegalArgumentException("Usuario invalido");
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("INSERT INTO usuarios(login,password,nombre,rol) VALUES(?,?,?,?)")) {
            p.setString(1, login);
            p.setString(2, password);
            p.setString(3, nombre);
            p.setString(4, rol);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se guardo el usuario", e);
        }
    }

    public void eliminar(int id) {
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("DELETE FROM usuarios WHERE id=?")) {
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se elimino el usuario", e);
        }
    }

    public void actualizar(int id, String login, String password, String nombre, String rol) {
        if (login.isBlank() || password.isBlank() || nombre.isBlank() || rol.isBlank()) throw new IllegalArgumentException("Usuario invalido");
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("UPDATE usuarios SET login=?, password=?, nombre=?, rol=? WHERE id=?")) {
            p.setString(1, login);
            p.setString(2, password);
            p.setString(3, nombre);
            p.setString(4, rol);
            p.setInt(5, id);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se actualizo el usuario", e);
        }
    }

    public Optional<Usuario> login(String login, String password) {
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("SELECT * FROM usuarios WHERE login=? AND password=?")) {
            p.setString(1, login);
            p.setString(2, password);
            ResultSet r = p.executeQuery();
            return r.next() ? Optional.of(usuario(r)) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("No se valido usuario", e);
        }
    }

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        try (Connection c = Database.connect(); ResultSet r = c.createStatement().executeQuery("SELECT * FROM usuarios ORDER BY id")) {
            while (r.next()) lista.add(usuario(r));
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("No se leyeron usuarios", e);
        }
    }

    private Usuario usuario(ResultSet r) throws SQLException {
        return new Usuario(r.getInt("id"), r.getString("login"), r.getString("password"), r.getString("nombre"), r.getString("rol"));
    }
}
