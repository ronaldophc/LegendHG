package com.ronaldophc.database;

import com.ronaldophc.LegendHG;
import com.ronaldophc.setting.Settings;
import com.ronaldophc.util.Logger;
import lombok.Getter;

import java.sql.*;
import java.util.Objects;

public class MySQLManager {

    private static Connection connection;
    static String host;
    static String database;
    static String username;
    static String password;
    static int port;
    @Getter
    public static boolean isActive = true;

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

    public static Connection getConnection() throws SQLException {
        try {

            if (connection != null && !connection.isClosed() && connection.isValid(0)) {
                return connection;
            }

            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?characterEncoding=UTF-8&connectTimeout=10000&serverTimezone=America/Sao_Paulo";

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
            String sql = "CREATE TABLE IF NOT EXISTS players (uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(16), password VARCHAR(255), kills INT, deaths INT, wins INT, scoreboard VARCHAR(15), tag VARCHAR(10), chat BOOLEAN, tell BOOLEAN, cooldown_type VARCHAR(10), ip_address VARCHAR(45), registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
            statement.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS games (id INT PRIMARY KEY AUTO_INCREMENT," +
                    " winner VARCHAR(36)," +
                    " kit1_winner VARCHAR(36)," +
                    " kit2_winner VARCHAR(36)," +
                    " kills INT, players INT," +
                    " type INT," +
                    " started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    " ended_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS player_login (" +
                    "uuid VARCHAR(36) PRIMARY KEY, " +
                    "logged_in BOOLEAN NOT NULL, " +
                    "FOREIGN KEY (uuid) REFERENCES players(uuid))";
            statement.execute(sql);

            sql = "UPDATE player_login SET logged_in = false";
            statement.execute(sql);

            // Create bans table with additional columns
            // Create bans table with additional columns
            sql = "CREATE TABLE IF NOT EXISTS bans (" +
                    "uuid VARCHAR(36) PRIMARY KEY, " +
                    "end_time BIGINT NOT NULL, " +
                    "banned_by VARCHAR(36) NOT NULL, " +
                    "reason TEXT NOT NULL, " +
                    "active BOOLEAN DEFAULT FALSE, " +
                    "banned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.execute(sql);

            // Create mutes table with additional columns
            sql = "CREATE TABLE IF NOT EXISTS mutes (" +
                    "uuid VARCHAR(36) PRIMARY KEY, " +
                    "end_time BIGINT NOT NULL, " +
                    "muted_by VARCHAR(36) NOT NULL, " +
                    "reason TEXT NOT NULL, " +
                    "active BOOLEAN DEFAULT FALSE, " +
                    "muted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            statement.execute(sql);

            // Create IP bans table
            sql = "CREATE TABLE IF NOT EXISTS ip_bans (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "ip_address VARCHAR(45) NOT NULL, " +
                    "end_time BIGINT NOT NULL, " +
                    "banned_by VARCHAR(36) NOT NULL, " +
                    "reason TEXT NOT NULL, " +
                    "active BOOLEAN DEFAULT TRUE, " +
                    "banned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "unbanned_at TIMESTAMP)";
            statement.execute(sql);
        } catch (SQLException e) {
            Logger.logError(e.getMessage());
            LegendHG.logger.info("Failed to create tables MYSQL");
        }
    }

    // ----- GETTERS by UUID ----- //

    public static boolean getBoolean(String uuid, String table, String field) throws SQLException {
        if (!isActive()) return false;
        String query = "SELECT " + field + " FROM " + table + " WHERE uuid = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean(field);
            }
            return true;
        } catch (SQLException e) {
            Logger.logError("query: " + query + ". " + e.getMessage());
            throw e;
        }
    }

    public static int getInt(String uuid, String table, String field) throws SQLException {
        if (!isActive()) return 0;
        String query = "SELECT " + field + " FROM " + table + " WHERE uuid = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(field);
            }
            return 0;
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve int " + field + " from " + uuid + ": " + e.getMessage());
            throw e;
        }
    }

    public static String getString(String uuid, String table, String field) throws SQLException {
        if (!isActive()) return null;
        String query = "SELECT " + field + " FROM " + table + " WHERE uuid = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString(field);
            }
            return null;
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve string " + field + " from " + uuid + ": " + e.getMessage());
            throw e;
        }
    }

    // ----- GETTERS ----- //

    public static String getStringByName(String name, String table, String field) throws SQLException {
        if (!isActive()) return null;
        String query = "SELECT " + field + " FROM " + table + " WHERE name = ?";

        try (PreparedStatement preparedStatement = Objects.requireNonNull(getConnection()).prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString(field);
            }
            return null;
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve string " + field + " from " + name + ": " + e.getMessage());
            throw e;
        }
    }

    // ----- SETTERS ----- //

    public static void setInt(String uuid, String table, String field, int value) throws SQLException {
        if (!isActive()) return;
        String query = "UPDATE " + table + " SET " + field + " = ? WHERE uuid = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.logError("Failed to update int: " + e.getMessage());
            throw e;
        }
    }

    public static void setString(String uuid, String table, String field, String value) throws SQLException {
        if (!isActive()) return;
        String query = "UPDATE " + table + " SET " + field + " = ? WHERE uuid = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.logError("Failed to update string: " + e.getMessage());
            throw e;
        }
    }

    public static void setBoolean(String uuid, String table, String field, Boolean value) throws SQLException {
        if (!isActive()) return;
        String query = "UPDATE " + table + " SET " + field + " = ? WHERE uuid = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.setBoolean(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.logError("Failed to update boolean: " + e.getMessage());
            throw e;
        }
    }
}