package com.ronaldophc.feature.battleonthesummit;

import com.ronaldophc.LegendHG;
import com.ronaldophc.feature.Schematic;
import com.ronaldophc.helper.Helper;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
public class SummitManager {

    private final List<Account> accounts = new ArrayList<>();
    private static SummitManager instance = new SummitManager();
    private final Location location = new Location(Bukkit.getWorld("world"), 1000, 100, 1000);
    public final ItemStack kangarooItem = new ItemManager(Material.FIREWORK, Util.color3 + "Kangaroo")
                        .setUnbreakable()
                        .build();
    public final ItemStack switcherItem = new ItemManager(Material.SNOW_BALL, Util.color3 + "Switcher").build();
    public final ItemStack grandpaItem = new ItemManager(Material.STICK, Util.color3 + "Grandpa")
                        .addEnchantment(Enchantment.KNOCKBACK, 2)
                        .setUnbreakable()
                        .build();

    public SummitManager() {
    }

    public void initialize() {
        LegendHG.logger.info("Initializing SummitManager");
//        Helper.loadChunks(750, 1250, 750, 1250);
//        Helper.clearBlocks(750, 1250, 750, 1250, 0, 120);
        createSchematic();

        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<Account> iterator = accounts.iterator();
                while (iterator.hasNext()) {
                    Account account = iterator.next();
                    Player player = account.getPlayer();
                    if (!player.isOnline()) {
                        iterator.remove();
                        continue;
                    }
                    if (player.getLocation().getY() < 20) {
                        playerLose(player);
                    }
                }
            }
        }.runTaskTimer(LegendHG.getInstance(), 0L, 20L);
        LegendHG.logger.info("Initialized SummitManager successfully");
    }

    public void createSchematic() {
        Schematic.getInstance().createSchematic(location.getWorld(), location, "RDC");
    }

    public void playerJoin(Player player) {
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (accounts.contains(account)) {
            player.sendMessage(Util.error + "Você já está no minigame");
            return;
        }

        player.getInventory().clear();
        player.getInventory().addItem(kangarooItem, switcherItem, grandpaItem);
        player.updateInventory();
        accounts.add(account);
        player.teleport(location.clone().add(37, 82, 38));
    }

    public void playerLose(Player player) {
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!accounts.contains(account)) {
            return;
        }

        accounts.remove(account);
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }

    public static SummitManager getInstance() {
        if (instance == null) {
            instance = new SummitManager();
        }
        return instance;
    }
}
