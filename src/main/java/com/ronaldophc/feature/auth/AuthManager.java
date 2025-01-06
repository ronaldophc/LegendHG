package com.ronaldophc.feature.auth;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthManager {

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
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        GameState gameState = LegendHG.getGameStateManager().getGameState();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        boolean isAlive = account.isAlive();

        switch (gameState) {
            case COUNTDOWN:
                PlayerHelper.resetPlayerAfterLogin(player);
                account.setAlive(true);
                break;
            case INVINCIBILITY:
                if (isAlive) return;
                PlayerHelper.resetPlayerState(player);
                account.setAlive(true);
                if (!player.getInventory().contains(Material.COMPASS)) {
                    player.getInventory().addItem(new ItemStack(Material.COMPASS));
                }
                break;
            default:
                if (isAlive) return;
                account.setSpectator(true);
                PlayerHelper.preparePlayerToSpec(player);
                break;
        }
    }

    public static void kickPlayersNotLoggedIn() {
        for (Player player : LegendHG.getInstance().getServer().getOnlinePlayers()) {
            Account account = AccountManager.getInstance().getOrCreateAccount(player);
            if (!account.isLoggedIn()) {
                player.kickPlayer(Util.title + " > " + Util.error + "O jogo começou e você não entrou.");
            }
        }
    }
}
