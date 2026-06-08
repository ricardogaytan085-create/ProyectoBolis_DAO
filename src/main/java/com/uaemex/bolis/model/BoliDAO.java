package com.uaemex.bolis.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoliDAO {
    private static final Logger LOG = Logger.getLogger(BoliDAO.class.getName());

    public void guardar(String sabor, double precio, int stock) {
        validar(sabor, precio, stock);
        run("No se guardo el boli", () -> Database.update("INSERT INTO bolis(sabor,precio,stock) VALUES(?,?,?)", p -> {
            p.setString(1, sabor);
            p.setDouble(2, precio);
            p.setInt(3, stock);
        }));
    }

    public void actualizar(int id, String sabor, double precio, int stock) {
        validarId(id);
        validar(sabor, precio, stock);
        run("No se actualizo el boli", () -> Database.update("UPDATE bolis SET sabor=?, precio=?, stock=? WHERE id=?", p -> {
            p.setString(1, sabor);
            p.setDouble(2, precio);
            p.setInt(3, stock);
            p.setInt(4, id);
        }));
    }

    public void eliminar(int id) {
        validarId(id);
        run("No se elimino el boli", () -> Database.update("DELETE FROM bolis WHERE id=?", p -> p.setInt(1, id)));
    }

    public Optional<Boli> buscar(int id) {
        validarId(id);
        return run("No se leyo el boli", () -> Database.find("SELECT * FROM bolis WHERE id=?", p -> p.setInt(1, id), this::boli));
    }

    public List<Boli> listar() {
        return run("No se leyeron bolis", () -> Database.list("SELECT * FROM bolis ORDER BY id", this::boli));
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
