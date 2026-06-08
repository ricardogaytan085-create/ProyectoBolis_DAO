package com.uaemex.bolis.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Database {
    @FunctionalInterface
    public interface Binder {
        void bind(PreparedStatement p) throws SQLException;
    }

    @FunctionalInterface
    public interface RowMapper<T> {
        T map(ResultSet r) throws SQLException;
    }

    @FunctionalInterface
    public interface SqlWork<T> {
        T run(Connection c) throws SQLException;
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(System.getProperty("bolis.db", "jdbc:derby:bolisDB;create=true"));
    }

    public static int update(String sql, Binder binder) throws SQLException {
        try (Connection c = connect()) {
            return update(c, sql, binder);
        }
    }

    public static int update(Connection c, String sql, Binder binder) throws SQLException {
        try (PreparedStatement p = c.prepareStatement(sql)) {
            if (binder != null) binder.bind(p);
            return p.executeUpdate();
        }
    }

    public static int requireUpdated(int rows, String message) {
        if (rows == 0) throw new IllegalArgumentException(message);
        return rows;
    }

    public static <T> Optional<T> find(String sql, Binder binder, RowMapper<T> mapper) throws SQLException {
        try (Connection c = connect()) {
            return find(c, sql, binder, mapper);
        }
    }

    public static <T> Optional<T> find(Connection c, String sql, Binder binder, RowMapper<T> mapper) throws SQLException {
        try (PreparedStatement p = c.prepareStatement(sql)) {
            if (binder != null) binder.bind(p);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? Optional.of(mapper.map(r)) : Optional.empty();
            }
        }
    }

    public static <T> List<T> list(String sql, RowMapper<T> mapper) throws SQLException {
        return list(sql, null, mapper);
    }

    public static <T> List<T> list(String sql, Binder binder, RowMapper<T> mapper) throws SQLException {
        List<T> rows = new ArrayList<>();
        try (Connection c = connect(); PreparedStatement p = c.prepareStatement(sql)) {
            if (binder != null) binder.bind(p);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) rows.add(mapper.map(r));
                return rows;
            }
        }
    }

    public static <T> T transaction(SqlWork<T> work) throws SQLException {
        try (Connection c = connect()) {
            c.setAutoCommit(false);
            try {
                T result = work.run(c);
                c.commit();
                return result;
            } catch (RuntimeException | SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }
}
