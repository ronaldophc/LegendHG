package com.ronaldophc.feature.admin;

import com.ronaldophc.game.GameHelper;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class AdminManager {

    private static AdminManager instance = new AdminManager();
    private final Map<Player, ItemStack[]> playerItens;
    private final Map<Player, ItemStack[]> playerArmors;

    private AdminManager() {
        playerItens = new HashMap<>();
        playerArmors = new HashMap<>();
    }

    public void addPlayerInventory(Player player, ItemStack[] inventoryContents, ItemStack[] armorContents) {
        playerItens.put(player, inventoryContents);
        playerArmors.put(player, armorContents);
    }

    public ItemStack[] getPlayerItens(Player player) {
        return playerItens.get(player);
    }

    public ItemStack[] getPlayerArmors(Player player) {
        return playerArmors.get(player);
    }

    public static ItemStack playersCompass = new ItemManager(Material.COMPASS, Util.color3 + "Jogadores")
            .setLore(Collections.singletonList(ChatColor.GRAY + "Clique para abrir o menu de jogadores.")).build();

    public static String inventoryName = Util.title + Util.color2 + " Players";
    
    public static void openInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 6 * 9, inventoryName);
        for (Player target : AccountManager.getInstance().getPlayersAlive()) {
            Account targetAccount = AccountManager.getInstance().getOrCreateAccount(target);
            List<String> lore = getLore(targetAccount);

            ItemStack playerSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta playerSkullMeta = (SkullMeta) playerSkull.getItemMeta();
            playerSkullMeta.setOwner(target.getName());
            playerSkullMeta.setDisplayName(Util.color1 + target.getName());
            playerSkullMeta.setLore(lore);
            playerSkull.setItemMeta(playerSkullMeta);

            inv.addItem(playerSkull);
        }

        player.openInventory(inv);
    }

    public boolean restorePlayerInventory(Player player) {
        ItemStack[] savedInventory = playerItens.get(player);
        ItemStack[] savedArmor = playerArmors.get(player);

        if (savedInventory != null && savedArmor != null) {
            player.getInventory().setContents(savedInventory);
            player.getInventory().setArmorContents(savedArmor);
            return true;
        }
        return false;
    }

    public void savePlayerInventory(Player player) {
        playerItens.put(player, player.getInventory().getContents());
        playerArmors.put(player, player.getInventory().getArmorContents());
    }

    private static List<String> getLore(Account targetAccount) {
        int kills = targetAccount.getKills();
        int totalKills = targetAccount.getTotalKills();
        int deaths = targetAccount.getDeaths();
        String primary = targetAccount.getKits().getPrimary().getName();

        List<String> lore = new ArrayList<>();

        lore.add(ChatColor.GRAY + "Kills: " + Util.success + kills);

        lore.add(ChatColor.GRAY + "Kit: " + Util.success + primary);

        if (GameHelper.getInstance().isTwoKits()) {
            String secondary = targetAccount.getKits().getSecondary().getName();
            if (secondary != null) {
                lore.add(ChatColor.GRAY + "Kit 2: " + Util.success + secondary);
            }
        }

        lore.add(ChatColor.GRAY + "Total Kills: " + Util.success + totalKills);

        lore.add(ChatColor.GRAY + "Deaths: " + Util.error + deaths);

        lore.add(Util.admin + "Clique para teleportar!");

        return lore;
    }

    public static synchronized AdminManager getInstance() {
        if (instance == null) {
            instance = new AdminManager();
        }
        return instance;
    }
}
