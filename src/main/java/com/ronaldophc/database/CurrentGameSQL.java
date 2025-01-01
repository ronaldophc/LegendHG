package com.ronaldophc.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Logger;

public class CurrentGameSQL {

    public static void deleteAllCurrentGameStats() throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return;
        String query = "DELETE FROM current_game_stats";
        Connection connection = LegendHG.getMySQLManager().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
            preparedStatement.close();
            Logger.debugMySql("Deleted all current game stats");
        } catch (SQLException e) {
            Logger.logError("Failed to delete all current game stats: " + e.getMessage());
            throw e;
        }
    }

    public static boolean isCurrentGameStatsCreated(Player player, int gameId) throws SQLException {
        String query = "SELECT COUNT(*) FROM current_game_stats WHERE uuid = ? AND game_id = ?";
        Connection connection = LegendHG.getMySQLManager().getConnection();
        String uuid = player.getUniqueId().toString();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            preparedStatement.setInt(2, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            preparedStatement.close();
            return false;
        } catch (SQLException e) {
            Logger.logError("Failed to check if current game stats are created: " + e.getMessage());
            throw e;
        }
    }

    public static void createCurrentGameStats(Player player, int gameId) throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return;
        if (isCurrentGameStatsCreated(player, gameId)) return;
        String query = "INSERT INTO current_game_stats (uuid, game_id, kills, deaths) VALUES (?, ?, 0, 0)";
        Connection connection = LegendHG.getMySQLManager().getConnection();
        String uuid = player.getUniqueId().toString();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            preparedStatement.setInt(2, gameId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            Logger.debugMySql("Created current game stats for player: " + player.getName());
        } catch (SQLException e) {
            Logger.logError("Failed to create current game stats: " + e.getMessage());
            throw e;
        }
    }

    public static void addCurrentGameKill(Player player, int gameId) throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return;
        String query = "UPDATE current_game_stats SET kills = ? where uuid = ? AND game_id = ?";
        Connection connection = LegendHG.getMySQLManager().getConnection();
        String uuid = player.getUniqueId().toString();
        int kills = getCurrentGameKills(player, gameId) + 1;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, kills);
            preparedStatement.setString(2, uuid);
            preparedStatement.setInt(3, gameId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            Logger.debugMySql("Updated current game kills for player: " + player.getName());
        } catch (SQLException e) {
            Logger.logError("Failed to update current game kills: " + e.getMessage());
            throw e;
        }
    }

    public static int getCurrentGameKills(Player player, int gameId) throws SQLException {
        String query = "SELECT kills FROM current_game_stats WHERE uuid = ? AND game_id = ?";
        Connection connection = LegendHG.getMySQLManager().getConnection();
        String uuid = player.getUniqueId().toString();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            preparedStatement.setInt(2, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("kills");
            }
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve current game kills: " + e.getMessage());
            throw e;
        }
    }

    public static void addCurrentGameDeath(Player player, int gameId) throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return;
        String query = "UPDATE current_game_stats SET deaths = ? where uuid = ? AND game_id = ?";
        Connection connection = LegendHG.getMySQLManager().getConnection();
        String uuid = player.getUniqueId().toString();
        int death = getCurrentGameDeaths(player, gameId) + 1;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, death);
            preparedStatement.setString(2, uuid);
            preparedStatement.setInt(3, gameId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            Logger.debugMySql("Updated current game kills for player: " + player.getName());
        } catch (SQLException e) {
            Logger.logError("Failed to update current game kills: " + e.getMessage());
            throw e;
        }
    }

    public static int getCurrentGameDeaths(Player player, int gameId) throws SQLException {
        String query = "SELECT deaths FROM current_game_stats WHERE uuid = ? AND game_id = ?";
        Connection connection = LegendHG.getMySQLManager().getConnection();
        String uuid = player.getUniqueId().toString();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, uuid);
            preparedStatement.setInt(2, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("deaths");
            }
            preparedStatement.close();
            return 0;
        } catch (SQLException e) {
            Logger.logError("Failed to retrieve current game deaths: " + e.getMessage());
            throw e;
        }
    }
}
