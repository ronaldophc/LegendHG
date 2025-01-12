package com.ronaldophc.game;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.MySQL.PlayerField;
import com.ronaldophc.constant.MySQL.Tables;
import com.ronaldophc.database.GameSQL;
import com.ronaldophc.database.MySQLManager;
import com.ronaldophc.feature.auth.AuthManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
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
                createCakePlatform(player);
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

    private static void createCakePlatform(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        int platformY = world.getHighestBlockYAt(loc) + 30;

        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                Location glassLoc = loc.clone().add(x, platformY - loc.getY(), z);
                world.getBlockAt(glassLoc).setType(Material.GLASS);
                world.getBlockAt(glassLoc.add(0, 1, 0)).setType(Material.CAKE_BLOCK);
            }
        }
        player.teleport(loc.add(0, platformY - loc.getY() + 2, 0));
        launchFireworks(player);
    }

    private static void launchFireworks(Player player) {
        World world = player.getWorld();
        Location loc = player.getLocation();
        for (int i = 0; i < 10; i++) {
            Firework firework = world.spawn(loc, Firework.class);
            FireworkMeta meta = firework.getFireworkMeta();
            meta.addEffect(FireworkEffect.builder().withColor(Color.RED, Color.BLUE).with(FireworkEffect.Type.BALL).trail(true).flicker(true).build());
            meta.setPower(1);
            firework.setFireworkMeta(meta);
        }
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
