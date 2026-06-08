package com.uaemex.bolis.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void init() {
        try (Connection c = Database.connect(); Statement s = c.createStatement()) {
            table(s, "CREATE TABLE usuarios (id INT GENERATED ALWAYS AS IDENTITY, login VARCHAR(40) UNIQUE NOT NULL, password VARCHAR(60) NOT NULL, nombre VARCHAR(80) NOT NULL, rol VARCHAR(20) NOT NULL, PRIMARY KEY(id))");
            table(s, "CREATE TABLE asistencia (id INT GENERATED ALWAYS AS IDENTITY, usuario_id INT NOT NULL REFERENCES usuarios(id), fecha DATE NOT NULL, hora_entrada TIME, hora_salida TIME, PRIMARY KEY(id))");
            table(s, "CREATE TABLE bolis (id INT GENERATED ALWAYS AS IDENTITY, sabor VARCHAR(50) UNIQUE NOT NULL, precio DECIMAL(8,2) NOT NULL, stock INT NOT NULL, PRIMARY KEY(id))");
            table(s, "CREATE TABLE ventas (id INT GENERATED ALWAYS AS IDENTITY, boli_id INT NOT NULL REFERENCES bolis(id), usuario_id INT NOT NULL REFERENCES usuarios(id), cantidad INT NOT NULL, total_venta DECIMAL(10,2) NOT NULL, fecha_hora TIMESTAMP NOT NULL, PRIMARY KEY(id))");
            seed(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void table(Statement s, String sql) throws SQLException {
        try {
            s.executeUpdate(sql);
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) throw e;
        }
    }

    private static void seed(Connection c) throws SQLException {
        if (empty(c, "usuarios")) {
            try (PreparedStatement p = c.prepareStatement("INSERT INTO usuarios(login,password,nombre,rol) VALUES('admin','admin','Administrador','Administrador')")) {
                p.executeUpdate();
            }
        }
        if (empty(c, "bolis")) {
            try (PreparedStatement p = c.prepareStatement("INSERT INTO bolis(sabor,precio,stock) VALUES(?,?,?)")) {
                Object[][] data = {{"Limon", 12, 20}, {"Mango", 12, 20}, {"Fresa", 12, 20}};
                for (Object[] b : data) {
                    p.setString(1, (String) b[0]);
                    p.setInt(2, (int) b[1]);
                    p.setInt(3, (int) b[2]);
                    p.executeUpdate();
                }
            }
        }
    }

    private static boolean empty(Connection c, String table) throws SQLException {
        try (ResultSet r = c.createStatement().executeQuery("SELECT COUNT(*) FROM " + table)) {
            r.next();
            return r.getInt(1) == 0;
        }
    }
}
