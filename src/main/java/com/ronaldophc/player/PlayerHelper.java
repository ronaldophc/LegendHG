package com.ronaldophc.player;

import com.ronaldophc.game.GameHelper;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.setting.Settings;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PlayerHelper {

    public static boolean verifyMinPlayers() {
        if (AccountManager.getInstance().getPlayersAlive().size() >= Settings.getInstance().getInt("MinPlayers")) {
            return true;
        }
        Bukkit.broadcastMessage(Util.errorServer + "Jogadores insuficientes para começar, reiniciando a contagem");
        return false;
    }

    public static void teleportPlayerToSpawnLocation(Player player) {
        World world = Bukkit.getWorld("world");
        if (world == null) {
            Bukkit.getLogger().severe("World 'world' not found. Cannot teleport player to spawn location.");
            return;
        }
        Location spawnLocation = getRandomSpawnLocation(world);
        player.teleport(spawnLocation);
    }

    private static Location getRandomSpawnLocation(World world) {
        int spawnX = new Random().nextInt(20) - (10);
        int spawnZ = new Random().nextInt(20) - (10);
        int y = world.getHighestBlockYAt(spawnX, spawnZ);
        return new Location(world, spawnX, y + 1, spawnZ);
    }

    public static void resetPlayerState(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setExp(0.0F);
        player.setLevel(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.getActivePotionEffects().clear();
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.updateInventory();
    }

    public static void addItemsToStartGame(Player player) {
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        account.setVanish(false);
        account.setBuild(false);
        account.setSeeSpecs(false);
        player.getInventory().clear();
        if (!player.getInventory().contains(Material.COMPASS)) {
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
        account.getKits().getPrimary().apply(player);
        account.getKits().getSecondary().apply(player);
        player.updateInventory();
    }

    public static void preparePlayerToSpec(Player player) {
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setExp(0.0F);
        player.setLevel(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.getActivePotionEffects().clear();
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.updateInventory();
        player.setAllowFlight(true);
        player.setFlying(true);
    }

    public static void addStartItens(Player player) {
        player.getInventory().addItem(new ItemManager(Material.CHEST, ChatColor.YELLOW + "Kit 1").addEnchantment(Enchantment.DURABILITY, 1).build());
        if (GameHelper.getInstance().getKits() == 2) {
            player.getInventory().addItem(new ItemManager(Material.CHEST, ChatColor.YELLOW + "Kit 2").addEnchantment(Enchantment.DURABILITY, 2).build());
        }

        player.getInventory().setItem(6, new ItemManager(Material.BLAZE_ROD, ChatColor.YELLOW + "Minigame").build());

        player.getInventory().setItem(8, new ItemManager(Material.NAME_TAG, ChatColor.YELLOW + "Ajustes").build());
    }
}