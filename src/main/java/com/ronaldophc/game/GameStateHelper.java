package com.ronaldophc.game;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.MySQL.PlayerField;
import com.ronaldophc.constant.MySQL.Tables;
import com.ronaldophc.database.GameSQL;
import com.ronaldophc.database.MySQLManager;
import com.ronaldophc.feature.auth.AuthManager;
import com.ronaldophc.util.Util;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class GameStateHelper {

    public static void finishGame() {

        new BukkitRunnable() {

            @Override
            public void run() {
                Bukkit.broadcastMessage(Util.color1 + "O jogo acabou!");
                Player player = AccountManager.getInstance().getPlayersAlive().get(0);
                String name = player.getName();
                Bukkit.broadcastMessage(Util.color1 + "O vencedor foi: " + Util.success + Util.bold + name);
                GameHelper.createCakePlatform(player);
                preparePlayerToFinish(player);
                try {
                    Account account = AccountManager.getInstance().getOrCreateAccount(player);
                    int wins = account.getWins();
                    MySQLManager.setInt(player.getUniqueId().toString(), Tables.PLAYER.getTableName(), PlayerField.WINS.getFieldName(), wins + 1);
                    GameSQL.updateGameWinner(player);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }.runTaskLater(LegendHG.getInstance(), 40);

    }



    private static void preparePlayerToFinish(Player player) {
        player.closeInventory();
        player.getInventory().clear();
        player.setHealth(20);
        player.getInventory().setItem(1, new ItemStack(Material.WATER_BUCKET));
        player.updateInventory();
    }

    public static void preparePlayerToStart() {
        AuthManager.kickPlayersNotLoggedIn();
        for (Player player : AccountManager.getInstance().getPlayersAlive()) {
            player.sendMessage(Util.color1 + "O jogo começou!");
            PlayerHelper.teleportPlayerToSpawnLocation(player);

            PlayerHelper.resetPlayerState(player);
            PlayerHelper.addItemsToStartGame(player);
        }
    }

}
