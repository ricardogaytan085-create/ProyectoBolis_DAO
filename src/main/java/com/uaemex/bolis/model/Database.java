package com.uaemex.bolis.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(System.getProperty("bolis.db", "jdbc:derby:bolisDB;create=true"));
    }
}
