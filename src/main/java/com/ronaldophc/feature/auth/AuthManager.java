package com.ronaldophc.feature.auth;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.PlayerAliveManager;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.player.PlayerSpectatorManager;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.entity.Player;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;

public class AuthManager {

    public static HashMap<UUID, Bool> playerLogged = new HashMap<>();

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyPassword(String password, String storedHash) {
        return hashPassword(password).equals(storedHash);
    }

    public static void loginPlayer(Player player) {
        GameState gameState = LegendHG.getGameStateManager().getGameState();
        boolean isAlive = PlayerAliveManager.getInstance().isPlayerAlive(player.getUniqueId());
        boolean isSpec = PlayerSpectatorManager.getInstance().isPlayerSpectating(player);
        PlayerAliveManager playersAlive = PlayerAliveManager.getInstance();
        PlayerSpectatorManager spectators = PlayerSpectatorManager.getInstance();

        switch (gameState) {
            case COUNTDOWN:
                PlayerHelper.resetPlayerAfterLogin(player);
                playersAlive.addPlayer(player);
                break;

            case INVINCIBILITY:
                if (isAlive) return;
                PlayerHelper.resetPlayerState(player);
                playersAlive.addPlayer(player);
                break;
            default:
                if (isAlive) return;
                if (isSpec) return;
                PlayerHelper.preparePlayerToSpec(player);
                spectators.addPlayer(player);
                break;
        }
    }

    public static void kickPlayersNotLoggedIn() {
        for (Player player : LegendHG.getInstance().getServer().getOnlinePlayers()) {
            try {
                if (!PlayerSQL.isPlayerLoggedIn(player)) {
                    player.kickPlayer(Util.title + " > " + Util.error + "O jogo começou e você não entrou.");
                }
            } catch (Exception e) {
                Logger.logError("Failed to kick player: " + e.getMessage());
            }
        }
    }
}
