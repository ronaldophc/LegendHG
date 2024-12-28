package com.ronaldophc.database;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.GameHelper;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.player.PlayerAliveManager;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GamesSQL {

    public static int createGame() throws SQLException {
        String query = "INSERT INTO games (winner, kit1_winner, kit2_winner, kills, players, type) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = LegendHG.getMySQLManager().getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, "");
            preparedStatement.setString(2, "");
            preparedStatement.setString(3, "");
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, PlayerAliveManager.getInstance().getPlayersAlive().size());
            preparedStatement.setInt(6, GameHelper.getInstance().getKits());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            preparedStatement.close();
            throw new SQLException("Creating game failed, no ID obtained.");

        } catch (SQLException e) {
            Logger.logError("Failed to create game: " + e.getMessage());
            throw e;
        }
    }


    public static void updateGameWinner(Player player) throws SQLException {
        if (!LegendHG.getMySQLManager().isActive()) return;
        String query = "UPDATE games SET winner = ?, kit1_winner = ?, kit2_winner = ?, kills = ? WHERE id = ?;";
        Connection connection = LegendHG.getMySQLManager().getConnection();
        KitManager kitManager = LegendHG.getKitManager();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, kitManager.getPlayerKitName(player));
            preparedStatement.setString(3, kitManager.getPlayerKitName2(player));
            preparedStatement.setInt(4, CurrentGameSQL.getCurrentGameKills(player, LegendHG.getGameId()));
            preparedStatement.setInt(5, LegendHG.getGameId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            Logger.logError("Failed to update game winner: " + e.getMessage());
        }
    }

}
