package com.ronaldophc.database;

import com.ronaldophc.LegendHG;
import com.ronaldophc.feature.auth.AuthManager;
import com.ronaldophc.constant.Tags;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.constant.Scores;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerSQL {

    public static void registerPlayer(Player player, String password) throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return;
        if (!isPlayerRegistered(player)) {

            Connection connection = LegendHG.getMySQLManager().getConnection();
            String uuid = player.getUniqueId().toString();
            String name = player.getName();
            String hashedPassword = AuthManager.hashPassword(password);
            String scoreboard = String.valueOf(Scores.COMPLETE);
            String tag = String.valueOf(Tags.NORMAL);
            String ip = String.valueOf(player.getAddress().getAddress().getHostAddress());
            String query = "INSERT INTO players (uuid, name, password, kills, deaths, wins, scoreboard, tag, ip_address) VALUES (?, ?, ?, 0, 0, 0, ?, ?, ?)";
            String loginQuery = "INSERT INTO player_login (uuid, name, logged_in) VALUES (?, ?, true)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 PreparedStatement loginStatement = connection.prepareStatement(loginQuery)) {
                preparedStatement.setString(1, uuid);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, hashedPassword);
                preparedStatement.setString(4, scoreboard);
                preparedStatement.setString(5, tag);
                preparedStatement.setString(6, ip);
                preparedStatement.executeUpdate();

                loginStatement.setString(1, uuid);
                loginStatement.setString(2, name);
                loginStatement.executeUpdate();

                preparedStatement.close();
                loginStatement.close();
                Logger.debugMySql("Registered new player: " + player.getName());
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
        if (!LegendHG.getMySQLManager().isActive()) return false;
        Connection connection = LegendHG.getMySQLManager().getConnection();
        String uuid = player.getUniqueId().toString();
        String query = "SELECT password FROM players WHERE uuid = ?";
        String updateQuery = "UPDATE player_login SET logged_in = true WHERE uuid = ?";

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

        } catch (SQLException e) {
            Logger.logError("Failed to login player: " + e.getMessage());
            throw e;
        }
        return false;
    }

    public static void logoutPlayer(Player player) throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return;
        Connection connection = LegendHG.getMySQLManager().getConnection();
        String uuid = player.getUniqueId().toString();
        String query = "SELECT logged_in FROM player_login WHERE uuid = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getBoolean("logged_in")) {
                    String updateQuery = "UPDATE player_login SET logged_in = false WHERE uuid = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, uuid);
                        updateStatement.executeUpdate();
                        updateStatement.close();
                    }
                }
            }
        } catch (SQLException e) {
            Logger.logError("Failed to logout player: " + e.getMessage());
        }
    }

    // ----- CHECKS ----- //

    public static boolean isPlayerLoggedIn(Player player) throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return false;
        Connection connection = LegendHG.getMySQLManager().getConnection();
        String uuid = player.getUniqueId().toString();
        String query = "SELECT logged_in FROM player_login WHERE uuid = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("logged_in");
            }
        } catch (SQLException e) {
            Logger.logError("Failed to check if player is logged in: " + e.getMessage());
            throw e;
        }
        return false;
    }

    public static boolean isPlayerRegistered(Player player) throws SQLException {
        String query = "SELECT COUNT(*) FROM players WHERE uuid = ?";
        Connection connection = LegendHG.getMySQLManager().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            String uuid = player.getUniqueId().toString();
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            preparedStatement.close();
            return false;
        } catch (SQLException e) {
            Logger.logError("Failed to check if player is registered: " + e.getMessage());
            throw e;
        }
    }

    // ----- UPDATE ----- //

    public static void addPlayerKill(Player player) throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return;
        String query = "UPDATE players SET kills = ? WHERE uuid = ?";
        Connection connection = LegendHG.getMySQLManager().getConnection();
        String uuid = player.getUniqueId().toString();
        int kills = getPlayerKills(player) + 1;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, kills);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            Logger.debugMySql("Updated kills for player: " + player.getName());
        } catch (SQLException e) {
            Logger.logError("Failed to update player kills: " + e.getMessage());
            throw e;
        }
    }

    public static void addPlayerDeath(Player player) throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return;
        String query = "UPDATE players SET deaths = ? WHERE uuid = ?";
        Connection connection = LegendHG.getMySQLManager().getConnection();
        String uuid = player.getUniqueId().toString();
        int deaths = getPlayerDeaths(player) + 1;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, deaths);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            Logger.debugMySql("Updated deaths for player: " + player.getName());
        } catch (SQLException e) {
            Logger.logError("Failed to update player deaths: " + e.getMessage());
            throw e;
        }
    }

    // ----- GETTERS ----- //

    public static int getPlayerKills(Player player) throws SQLException {
        String query = "SELECT kills FROM players WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = LegendHG.getMySQLManager().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("kills");
            }
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve player kills: " + e.getMessage());
            throw e;
        }
    }

    public static Scores getPlayerScore(Player player) throws SQLException {
        String query = "SELECT scoreboard FROM players WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = LegendHG.getMySQLManager().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Scores.valueOf(resultSet.getString("scoreboard"));
            }
            preparedStatement.close();
            return Scores.COMPLETE;
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve player kills: " + e.getMessage());
            throw e;
        }
    }

    public static Tags getPlayerTag(Player player) throws SQLException {
        String query = "SELECT tag FROM players WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = LegendHG.getMySQLManager().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Tags.valueOf(resultSet.getString("tag"));
            }

            preparedStatement.close();
            return Tags.NORMAL;
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve player kills: " + e.getMessage());
            throw e;
        }
    }

    public static int getPlayerDeaths(Player player) throws SQLException {
        String query = "SELECT deaths FROM players WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = LegendHG.getMySQLManager().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("deaths");
            }
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve player deaths: " + e.getMessage());
            throw e;
        }
    }

    public static int getPlayerWins(Player player) throws SQLException {
        String query = "SELECT wins FROM players WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = LegendHG.getMySQLManager().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("wins");
            }
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve player wins: " + e.getMessage());
            throw e;
        }
    }

    public static String getPlayerIpAddress(String name) throws SQLException {
        String query = "SELECT ip_address FROM players WHERE name = ?";
        Connection connection = LegendHG.getMySQLManager().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("ip_address");
            }
            preparedStatement.close();
            return null;
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve player IP address: " + e.getMessage());
            throw e;
        }
    }

    // ----- SETTERS ----- //

    public static void setPlayerWins(Player player) throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return;
        String query = "UPDATE players SET wins = ? WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = LegendHG.getMySQLManager().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, getPlayerWins(player) + 1);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            Logger.debugMySql("Updated wins for player: " + player.getName());
        } catch (SQLException e) {
            Logger.logError("Failed to update player wins: " + e.getMessage());
            throw e;
        }
    }

    public static void setPlayerScore(Player player, Scores score) throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return;
        String query = "UPDATE players SET scoreboard = ? WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = LegendHG.getMySQLManager().getConnection();

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

    public static void setPlayerTag(Player player, Tags tag) throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return;
        String query = "UPDATE players SET tag = ? WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = LegendHG.getMySQLManager().getConnection();

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

    public static void setPlayerIpAddress(Player player, String ipAddress) throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return;
        String query = "UPDATE players SET ip_address = ? WHERE uuid = ?";
        String uuid = player.getUniqueId().toString();
        Connection connection = LegendHG.getMySQLManager().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, ipAddress);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            Logger.debugMySql("Updated IP address for player: " + player.getName());
        } catch (SQLException e) {
            Logger.logError("Failed to update player IP address: " + e.getMessage());
            throw e;
        }
    }

}
