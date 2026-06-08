package com.uaemex.bolis.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseInitializer {
    private static final Logger LOG = Logger.getLogger(DatabaseInitializer.class.getName());

    public static void init() {
        try (Connection c = Database.connect(); Statement s = c.createStatement()) {
            table(s, "CREATE TABLE usuarios (id INT GENERATED ALWAYS AS IDENTITY, login VARCHAR(40) UNIQUE NOT NULL, password VARCHAR(160) NOT NULL, nombre VARCHAR(120) NOT NULL, nombres VARCHAR(80) NOT NULL, apellido_paterno VARCHAR(60) NOT NULL, apellido_materno VARCHAR(60) NOT NULL, fecha_nacimiento DATE NOT NULL, email VARCHAR(100) NOT NULL, telefono VARCHAR(20) NOT NULL, rol VARCHAR(20) NOT NULL, PRIMARY KEY(id))");
            table(s, "CREATE TABLE asistencia (id INT GENERATED ALWAYS AS IDENTITY, usuario_id INT NOT NULL REFERENCES usuarios(id), fecha DATE NOT NULL, hora_entrada TIME, hora_salida TIME, PRIMARY KEY(id))");
            table(s, "CREATE TABLE bolis (id INT GENERATED ALWAYS AS IDENTITY, sabor VARCHAR(50) UNIQUE NOT NULL, precio DECIMAL(8,2) NOT NULL, stock INT NOT NULL, PRIMARY KEY(id))");
            table(s, "CREATE TABLE ventas (id INT GENERATED ALWAYS AS IDENTITY, boli_id INT NOT NULL REFERENCES bolis(id), usuario_id INT NOT NULL REFERENCES usuarios(id), cantidad INT NOT NULL, total_venta DECIMAL(10,2) NOT NULL, fecha_hora TIMESTAMP NOT NULL, PRIMARY KEY(id))");
            resizePasswordColumn(s);
            column(s, "ALTER TABLE usuarios ADD COLUMN email VARCHAR(100) DEFAULT 'admin@local' NOT NULL");
            column(s, "ALTER TABLE usuarios ADD COLUMN telefono VARCHAR(20) DEFAULT '0000000000' NOT NULL");
            column(s, "ALTER TABLE usuarios ADD COLUMN nombres VARCHAR(80) DEFAULT 'Administrador' NOT NULL");
            column(s, "ALTER TABLE usuarios ADD COLUMN apellido_paterno VARCHAR(60) DEFAULT 'Sistema' NOT NULL");
            column(s, "ALTER TABLE usuarios ADD COLUMN apellido_materno VARCHAR(60) DEFAULT 'Local' NOT NULL");
            column(s, "ALTER TABLE usuarios ADD COLUMN fecha_nacimiento DATE DEFAULT DATE('2000-01-01') NOT NULL");
            seed(c);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se inicializo la base de datos", e);
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

    private static void column(Statement s, String sql) throws SQLException {
        try {
            s.executeUpdate(sql);
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) throw e;
        }
    }

    private static void resizePasswordColumn(Statement s) throws SQLException {
        try {
            s.executeUpdate("ALTER TABLE usuarios ALTER COLUMN password SET DATA TYPE VARCHAR(160)");
        } catch (SQLException e) {
            if (!"42X14".equals(e.getSQLState()) && !"X0Y25".equals(e.getSQLState())) throw e;
        }
    }

    private static void seed(Connection c) throws SQLException {
        if (empty(c, "usuarios")) {
            try (PreparedStatement p = c.prepareStatement("INSERT INTO usuarios(login,password,nombre,nombres,apellido_paterno,apellido_materno,fecha_nacimiento,email,telefono,rol) VALUES(?,?,?,?,?,?,?,?,?,?)")) {
                p.setString(1, "admin");
                p.setString(2, Passwords.hash("admin"));
                p.setString(3, "Administrador");
                p.setString(4, "Administrador");
                p.setString(5, "Sistema");
                p.setString(6, "Local");
                p.setDate(7, Date.valueOf(LocalDate.of(2000, 1, 1)));
                p.setString(8, "admin@local");
                p.setString(9, "0000000000");
                p.setString(10, "Administrador");
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
        try (Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT COUNT(*) FROM " + table)) {
            r.next();
            return r.getInt(1) == 0;
        }
    }
}
