package com.ronaldophc.feature.battleonthesummit;

import com.ronaldophc.LegendHG;
import com.ronaldophc.feature.Schematic;
import com.ronaldophc.util.Helper;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        if (!LegendHG.getInstance().devMode) {
            Helper.loadChunks(750, 1250, 750, 1250);
//            Helper.clearBlocks(750, 1250, 750, 1250, 0, 120);
        }
        createSchematic();

        LegendHG.logger.info("Initialized SummitManager successfully");
    }

    public void createSchematic() {
        File schematicFile = new File(LegendHG.getInstance().getDataFolder(), "RDC.schematic");
        Schematic.getInstance().createSchematic(location.getWorld(), location, schematicFile);
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
        int random = new Random().nextInt(14);
        player.teleport(location.clone().add(random, 0, random));

        player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.0f, 1.0f);
        player.getWorld().playEffect(player.getLocation(), Effect.HAPPY_VILLAGER,  1, 2);

        player.sendMessage(Util.color3 + "Você entrou no minigame RDC, derrube os outros e ganhe pontos!");
        player.sendMessage(Util.color3 + "Para sair digite /rdc");
    }

    public void playerLose(Player player) {
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!this.accounts.contains(account)) {
            return;
        }

        PlayerHelper.resetPlayerState(player);
        PlayerHelper.addStartItens(player);
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
