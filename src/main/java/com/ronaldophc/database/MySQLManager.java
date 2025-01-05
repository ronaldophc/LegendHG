package com.ronaldophc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.setting.Settings;

public class MySQLManager {

    private Connection connection;
    String host;
    String database;
    String username;
    String password;
    int port;
    boolean isActive = true;

    public MySQLManager() {
        if (!Settings.getInstance().exist("Database.Host") || !Settings.getInstance().exist("Database.Database") || !Settings.getInstance().exist("Database.User") || !Settings.getInstance().exist("Database.Password") || !Settings.getInstance().exist("Database.Port")) {
            isActive = false;
            return;
        }

        host = Settings.getInstance().getString("Database.Host");
        database = Settings.getInstance().getString("Database.Database");
        username = Settings.getInstance().getString("Database.User");
        password = Settings.getInstance().getString("Database.Password");
        port = Settings.getInstance().getInt("Database.Port");
    }

    public Connection getConnection() throws SQLException {
        try {
            if (host == null || database == null || username == null || password == null || port == 0) {
                return null;
            }

            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding=UTF-8&connectTimeout=10000";

//             Logger.debugMySql("Attempting to connect to database with URL: " + jdbcUrl);

            connection = DriverManager.getConnection(jdbcUrl, username, password);

            return connection;
        } catch (SQLException e) {
            Logger.logError("Failed to connect to database: " + e.getMessage());
            throw e;
        }
    }

    public void initializeDatabase() throws SQLException {
        if (!isActive) {
            return;
        }
        try (Statement statement = getConnection().createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS players (uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(16), password VARCHAR(255), kills INT, deaths INT, wins INT, scoreboard VARCHAR(15), tag VARCHAR(10), ip_address VARCHAR(45), registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
            statement.execute(sql);
            
            sql = "CREATE TABLE IF NOT EXISTS games (id INT PRIMARY KEY AUTO_INCREMENT, winner VARCHAR(36), kit1_winner VARCHAR(36), kit2_winner VARCHAR(36), kills INT, players INT, type INT, started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, ended_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS player_login (uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(16), logged_in BOOLEAN NOT NULL)";
            statement.execute(sql);

            sql = "UPDATE player_login SET logged_in = false";
            statement.execute(sql);
        } catch (SQLException e) {
            Logger.logError(e.getMessage());
            LegendHG.logger.info("Failed to create table MYSQL");
        }
    }


    public boolean isActive() {
        return isActive;
    }
}