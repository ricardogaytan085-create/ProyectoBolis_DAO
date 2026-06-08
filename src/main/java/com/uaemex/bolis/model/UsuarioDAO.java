package com.uaemex.bolis.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO {
    private static final Logger LOG = Logger.getLogger(UsuarioDAO.class.getName());

    public void guardar(String login, String password, String nombre, String rol) {
        validar(login, password, nombre, rol);
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("INSERT INTO usuarios(login,password,nombre,rol) VALUES(?,?,?,?)")) {
            p.setString(1, login);
            p.setString(2, Passwords.hash(password));
            p.setString(3, nombre);
            p.setString(4, rol);
            p.executeUpdate();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se guardo el usuario", e);
            throw new RuntimeException("No se guardo el usuario", e);
        }
    }

    public void eliminar(int id) {
        validarId(id);
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("DELETE FROM usuarios WHERE id=?")) {
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se elimino el usuario", e);
            throw new RuntimeException("No se elimino el usuario", e);
        }
    }

    public void actualizar(int id, String login, String password, String nombre, String rol) {
        validarId(id);
        validar(login, password, nombre, rol);
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("UPDATE usuarios SET login=?, password=?, nombre=?, rol=? WHERE id=?")) {
            p.setString(1, login);
            p.setString(2, Passwords.hash(password));
            p.setString(3, nombre);
            p.setString(4, rol);
            p.setInt(5, id);
            p.executeUpdate();
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se actualizo el usuario", e);
            throw new RuntimeException("No se actualizo el usuario", e);
        }
    }

    public Optional<Usuario> login(String login, String password) {
        if (login == null || login.isBlank()) throw new IllegalArgumentException("Login invalido");
        Passwords.requirePassword(password);
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement("SELECT * FROM usuarios WHERE login=?")) {
            p.setString(1, login);
            try (ResultSet r = p.executeQuery()) {
                if (!r.next() || !Passwords.matches(password, r.getString("password"))) return Optional.empty();
                Usuario usuario = usuario(r);
                if (!Passwords.hashed(usuario.password())) actualizarPassword(c, usuario.id(), password);
                return Optional.of(usuario);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se valido usuario", e);
            throw new RuntimeException("No se valido usuario", e);
        }
    }

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        try (Connection c = Database.connect();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery("SELECT * FROM usuarios ORDER BY id")) {
            while (r.next()) lista.add(usuario(r));
            return lista;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se leyeron usuarios", e);
            throw new RuntimeException("No se leyeron usuarios", e);
        }
    }

    private Usuario usuario(ResultSet r) throws SQLException {
        return new Usuario(r.getInt("id"), r.getString("login"), r.getString("password"), r.getString("nombre"), r.getString("rol"));
    }

    private void actualizarPassword(Connection c, int id, String password) throws SQLException {
        try (PreparedStatement p = c.prepareStatement("UPDATE usuarios SET password=? WHERE id=?")) {
            p.setString(1, Passwords.hash(password));
            p.setInt(2, id);
            p.executeUpdate();
        }
    }

    private void validar(String login, String password, String nombre, String rol) {
        Passwords.requirePassword(password);
        new Usuario(0, login, password, nombre, rol);
    }

    private void validarId(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id invalido");
    }
}
