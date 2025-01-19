package com.ronaldophc.database;

import com.ronaldophc.constant.CooldownType;
import com.ronaldophc.constant.MySQL.PlayerField;
import com.ronaldophc.constant.MySQL.Tables;
import com.ronaldophc.constant.Scores;
import com.ronaldophc.constant.Tags;
import com.ronaldophc.feature.auth.AuthManager;
import com.ronaldophc.util.Logger;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerSQL {

    public static void registerPlayer(Player player, String password) throws SQLException {
        if (!MySQLManager.isActive()) return;
        if (!isPlayerRegistered(player)) {

            Account account = AccountManager.getInstance().getOrCreateAccount(player);

            Connection connection = MySQLManager.getConnection();
            String uuid = account.getUUID().toString();
            String name = player.getName();
            String hashedPassword = AuthManager.hashPassword(password);
            String scoreboard = String.valueOf(Scores.COMPLETE);
            String tag = String.valueOf(Tags.NORMAL);
            String ip = String.valueOf(player.getAddress().getAddress().getHostAddress());
            String cooldown_type = String.valueOf(CooldownType.ACTION_BAR);
            String query = "INSERT INTO players (uuid, name, password, kills, deaths, wins, scoreboard, tag, chat, tell, cooldown_type, ip_address) VALUES (?, ?, ?, 0, 0, 0, ?, ?, ?, ?, ?, ?)";
            String loginQuery = "INSERT INTO player_login (uuid, logged_in) VALUES (?, true)";

            try {
                assert connection != null;
                try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                         PreparedStatement loginStatement = connection.prepareStatement(loginQuery)) {
                    preparedStatement.setString(1, uuid);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, hashedPassword);
                    preparedStatement.setString(4, scoreboard);
                    preparedStatement.setString(5, tag);
                    preparedStatement.setBoolean(6, true);
                    preparedStatement.setBoolean(7, true);
                    preparedStatement.setString(8, cooldown_type);
                    preparedStatement.setString(9, ip);
                    preparedStatement.executeUpdate();

                    loginStatement.setString(1, uuid);
                    loginStatement.executeUpdate();

                    Logger.debugMySql("Registered new player: " + player.getName());
                }
            } catch (SQLException e) {
                Logger.logError("Failed to register player: " + e.getMessage());
                throw e;
            }

            return;
        }
        Logger.debugMySql("Player: " + player.getName() + " is already registered.");
    }

    // ----- LOGIN ----- //

    public static boolean loginPlayer(Player player, String password) throws SQLException {
        if (!MySQLManager.isActive()) return false;
        Connection connection = MySQLManager.getConnection();
        String uuid = player.getUniqueId().toString();
        String query = "SELECT password FROM players WHERE uuid = ?";
        String updateQuery = "UPDATE player_login SET logged_in = true WHERE uuid = ?";

        try {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                     PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, uuid);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String storedHash = resultSet.getString("password");
                    if (AuthManager.verifyPassword(password, storedHash)) {
                        updateStatement.setString(1, uuid);
                        updateStatement.executeUpdate();
                        updateStatement.close();
                        return true;
                    }
                }

            }
        } catch (SQLException e) {
            Logger.logError("Failed to login player: " + e.getMessage());
            throw e;
        }
        return false;
    }

    public static void logoutPlayer(Player player) throws SQLException {
        if (!MySQLManager.isActive()) return;
        Connection connection = MySQLManager.getConnection();
        String uuid = player.getUniqueId().toString();
        String query = "SELECT logged_in FROM player_login WHERE uuid = ?";

        try {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, uuid);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getBoolean("logged_in")) {
                        String updateQuery = "UPDATE player_login SET logged_in = false WHERE uuid = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setString(1, uuid);
                            updateStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            Logger.logError("Failed to logout player: " + e.getMessage());
        }
    }

    // ----- CHECKS ----- //

    public static boolean isPlayerRegistered(Player player) throws SQLException {
        return isPlayerRegisteredByUUID(player.getUniqueId().toString());
    }

    public static boolean isPlayerRegisteredByUUID(String uuid) throws SQLException {
        String query = "SELECT COUNT(*) FROM players WHERE uuid = ?";
        Connection connection = MySQLManager.getConnection();

        try {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, uuid);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
                preparedStatement.close();
                return false;
            }
        } catch (SQLException e) {
            Logger.logError("Failed to check if player is registered: " + e.getMessage());
            throw e;
        }
    }

    public static boolean isPlayerRegisteredByName(String name) throws SQLException {
        String query = "SELECT COUNT(*) FROM players WHERE name = ?";
        Connection connection = MySQLManager.getConnection();

        try {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
                preparedStatement.close();
                return false;
            }
        } catch (SQLException e) {
            Logger.logError("Failed to check if player is registered: " + e.getMessage());
            throw e;
        }
    }

    // ----- UPDATE ----- //

    public static void addPlayerDeath(Player player) throws SQLException {
        if (!MySQLManager.isActive()) return;
        String query = "UPDATE players SET deaths = ? WHERE uuid = ?";
        Connection connection = MySQLManager.getConnection();
        String uuid = player.getUniqueId().toString();
        int deaths = MySQLManager.getInt(uuid, Tables.PLAYER.getTableName(), PlayerField.DEATHS.getFieldName()) + 1;

        try {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, deaths);
                preparedStatement.setString(2, uuid);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                Logger.debugMySql("Updated deaths for player: " + player.getName());
            }
        } catch (SQLException e) {
            Logger.logError("Failed to update player deaths: " + e.getMessage());
            throw e;
        }
    }

    // ----- GETTERS ----- //

    public static Scores getPlayerScore(Player player) throws SQLException {
        String query = "SELECT scoreboard FROM players WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = MySQLManager.getConnection();

        try {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, uuid);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return Scores.valueOf(resultSet.getString("scoreboard"));
                }
                preparedStatement.close();
                return Scores.COMPLETE;
            }
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve player kills: " + e.getMessage());
            throw e;
        }
    }

    public static CooldownType getPlayerCooldownType(Player player) throws SQLException {
        String query = "SELECT cooldown_type FROM players WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = MySQLManager.getConnection();

        try {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, uuid);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return CooldownType.valueOf(resultSet.getString("cooldown_type"));
                }
                preparedStatement.close();
                return CooldownType.ACTION_BAR;
            }
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve player cooldown_type: " + e.getMessage());
            throw e;
        }
    }

    public static Tags getPlayerTag(Player player) throws SQLException {
        if (!MySQLManager.isActive()) return Tags.NORMAL;
        String query = "SELECT tag FROM players WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = MySQLManager.getConnection();

        try {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, uuid);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return Tags.valueOf(resultSet.getString("tag"));
                }

                preparedStatement.close();
                return Tags.NORMAL;
            }
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve player kills: " + e.getMessage());
            throw e;
        }
    }

    public static UUID getPlayerUUID(Player player) throws SQLException {
        if (!MySQLManager.isActive()) return null;
        String query = "SELECT uuid FROM players WHERE name = ?";
        String name = player.getName();
        Connection connection = MySQLManager.getConnection();

        try {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return UUID.fromString(resultSet.getString("uuid"));
                }

                preparedStatement.close();
                return null;
            }
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve player kills: " + e.getMessage());
            throw e;
        }
    }

    // ----- SETTERS ----- //

    public static void setPlayerScore(Player player, Scores score) throws SQLException {
        if (!MySQLManager.isActive()) return;
        String query = "UPDATE players SET scoreboard = ? WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = MySQLManager.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, score.toString());
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            Logger.debugMySql("Updated Score for player: " + player.getName());
        } catch (SQLException e) {
            Logger.logError("Failed to update player score: " + e.getMessage());
            throw e;
        }
    }

    public static void setPlayerCooldownType(Player player, CooldownType cooldown_type) throws SQLException {
        if (!MySQLManager.isActive()) return;
        String query = "UPDATE players SET cooldown_type = ? WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = MySQLManager.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, cooldown_type.toString());
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            Logger.debugMySql("Updated cooldown_type for player: " + player.getName());
        } catch (SQLException e) {
            Logger.logError("Failed to update player cooldown_type: " + e.getMessage());
            throw e;
        }
    }

    public static void setPlayerTag(Player player, Tags tag) throws SQLException {
        if (!MySQLManager.isActive()) return;
        String query = "UPDATE players SET tag = ? WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = MySQLManager.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, tag.toString());
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            Logger.debugMySql("Updated Tag for player: " + player.getName());
        } catch (SQLException e) {
            Logger.logError("Failed to update player tag: " + e.getMessage());
            throw e;
        }
    }

}
