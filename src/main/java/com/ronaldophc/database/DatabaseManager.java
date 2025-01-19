package com.ronaldophc.database;

import com.ronaldophc.setting.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static Connection connection;

    public static void connect() throws SQLException {
        String host = Settings.getInstance().getString("Database.Host");
        String database = Settings.getInstance().getString("Database.Database");
        String username = Settings.getInstance().getString("Database.User");
        String password = Settings.getInstance().getString("Database.Password");
        int port = Settings.getInstance().getInt("Database.Port");
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding=UTF-8&connectTimeout=10000");
    }

    public static Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed() && connection.isValid(500)) {
            return connection;
        }
        connect();
        return connection;
    }

    public static void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
