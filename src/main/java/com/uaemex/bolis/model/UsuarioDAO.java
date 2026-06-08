package com.uaemex.bolis.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO {
    private static final Logger LOG = Logger.getLogger(UsuarioDAO.class.getName());

    public void guardar(String login, String password, String nombre, String rol) {
        validar(login, password, nombre, rol);
        run("No se guardo el usuario", () -> Database.update("INSERT INTO usuarios(login,password,nombre,rol) VALUES(?,?,?,?)", p -> {
            p.setString(1, login);
            p.setString(2, Passwords.hash(password));
            p.setString(3, nombre);
            p.setString(4, rol);
        }));
    }

    public void actualizar(int id, String login, String password, String nombre, String rol) {
        validarId(id);
        validar(login, password, nombre, rol);
        run("No se actualizo el usuario", () -> Database.update("UPDATE usuarios SET login=?, password=?, nombre=?, rol=? WHERE id=?", p -> {
            p.setString(1, login);
            p.setString(2, Passwords.hash(password));
            p.setString(3, nombre);
            p.setString(4, rol);
            p.setInt(5, id);
        }));
    }

    public void eliminar(int id) {
        validarId(id);
        run("No se elimino el usuario", () -> Database.update("DELETE FROM usuarios WHERE id=?", p -> p.setInt(1, id)));
    }

    public Optional<Usuario> login(String login, String password) {
        if (login == null || login.isBlank()) throw new IllegalArgumentException("Login invalido");
        Passwords.requirePassword(password);
        return run("No se valido usuario", () -> Database.transaction(c -> login(c, login, password)));
    }

    public List<Usuario> listar() {
        return run("No se leyeron usuarios", () -> Database.list("SELECT * FROM usuarios ORDER BY id", this::usuario));
    }

    private Optional<Usuario> login(Connection c, String login, String password) throws SQLException {
        Optional<Usuario> u = Database.find(c, "SELECT * FROM usuarios WHERE login=?", p -> p.setString(1, login), this::usuario);
        if (u.isEmpty() || !Passwords.matches(password, u.get().password())) return Optional.empty();
        if (!Passwords.hashed(u.get().password())) Database.update(c, "UPDATE usuarios SET password=? WHERE id=?", p -> {
            p.setString(1, Passwords.hash(password));
            p.setInt(2, u.get().id());
        });
        return u;
    }

    private Usuario usuario(ResultSet r) throws SQLException {
        return new Usuario(r.getInt("id"), r.getString("login"), r.getString("password"), r.getString("nombre"), r.getString("rol"));
    }

    private void validar(String login, String password, String nombre, String rol) {
        Passwords.requirePassword(password);
        new Usuario(0, login, password, nombre, rol);
    }

    private void validarId(int id) {
        if (id <= 0) throw new IllegalArgumentException("Id invalido");
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
