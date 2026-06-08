package com.uaemex.bolis.model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsistenciaDAO {
    private static final Logger LOG = Logger.getLogger(AsistenciaDAO.class.getName());

    public void entrada(int usuarioId) {
        validarId(usuarioId);
        run("No se registro entrada", () -> Database.update("INSERT INTO asistencia(usuario_id,fecha,hora_entrada) VALUES(?,?,?)", p -> {
            p.setInt(1, usuarioId);
            p.setDate(2, Date.valueOf(LocalDate.now()));
            p.setTime(3, Time.valueOf(LocalTime.now()));
        }));
    }

    public void salida(int usuarioId) {
        validarId(usuarioId);
        run("No se registro salida", () -> Database.transaction(c -> {
            int id = Database.find(c, "SELECT id FROM asistencia WHERE usuario_id=? AND hora_salida IS NULL ORDER BY id DESC FETCH FIRST ROW ONLY",
                    p -> p.setInt(1, usuarioId), r -> r.getInt(1))
                    .orElseThrow(() -> new RuntimeException("No hay entrada abierta"));
            Database.update(c, "UPDATE asistencia SET hora_salida=? WHERE id=?", p -> {
                p.setTime(1, Time.valueOf(LocalTime.now()));
                p.setInt(2, id);
            });
            return null;
        }));
    }

    public List<Asistencia> listar() {
        return run("No se leyeron asistencias", () -> Database.list("SELECT * FROM asistencia ORDER BY id", this::asistencia));
    }

    private Asistencia asistencia(ResultSet r) throws SQLException {
        return new Asistencia(r.getInt("id"), r.getInt("usuario_id"), r.getDate("fecha").toLocalDate(), time(r, "hora_entrada"), time(r, "hora_salida"));
    }

    private LocalTime time(ResultSet r, String col) throws SQLException {
        Time t = r.getTime(col);
        return t == null ? null : t.toLocalTime();
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
