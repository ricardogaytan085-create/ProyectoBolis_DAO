package com.uaemex.bolis.model;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaDAO {
    public void entrada(int usuarioId) {
        String sql = "INSERT INTO asistencia(usuario_id,fecha,hora_entrada) VALUES(?,?,?)";
        try (Connection c = Database.connect(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, usuarioId);
            p.setDate(2, Date.valueOf(LocalDate.now()));
            p.setTime(3, Time.valueOf(LocalTime.now()));
            p.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se registro entrada", e);
        }
    }

    public void salida(int usuarioId) {
        try (Connection c = Database.connect();
             PreparedStatement q = c.prepareStatement("SELECT id FROM asistencia WHERE usuario_id=? AND hora_salida IS NULL ORDER BY id DESC FETCH FIRST ROW ONLY")) {
            q.setInt(1, usuarioId);
            ResultSet r = q.executeQuery();
            if (!r.next()) throw new RuntimeException("No hay entrada abierta");
            try (PreparedStatement p = c.prepareStatement("UPDATE asistencia SET hora_salida=? WHERE id=?")) {
                p.setTime(1, Time.valueOf(LocalTime.now()));
                p.setInt(2, r.getInt(1));
                p.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se registro salida", e);
        }
    }

    public List<Asistencia> listar() {
        List<Asistencia> lista = new ArrayList<>();
        try (Connection c = Database.connect(); ResultSet r = c.createStatement().executeQuery("SELECT * FROM asistencia ORDER BY id")) {
            while (r.next()) lista.add(new Asistencia(r.getInt("id"), r.getInt("usuario_id"), r.getDate("fecha").toLocalDate(), time(r, "hora_entrada"), time(r, "hora_salida")));
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("No se leyeron asistencias", e);
        }
    }

    private LocalTime time(ResultSet r, String col) throws SQLException {
        Time t = r.getTime(col);
        return t == null ? null : t.toLocalTime();
    }
}
