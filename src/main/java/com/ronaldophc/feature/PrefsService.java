package com.ronaldophc.feature;

import com.ronaldophc.constant.CooldownType;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class PrefsService {

    public static ItemStack tellItem = new ItemManager(Material.SIGN, Util.color3 + "Mensagens Privadas")
            .setLore(Collections.singletonList(ChatColor.GRAY + "Ativar/desativar as mensagem privadas")).build();

    public static ItemStack tellActive = new ItemManager(Material.SULPHUR, ChatColor.RED + "Mensagens Privadas")
            .setLore(Collections.singletonList(ChatColor.GRAY + "Clique para ativar as mensagem privadas")).build();

    public static ItemStack tellDisable = new ItemManager(Material.GLOWSTONE_DUST, ChatColor.GREEN + "Mensagens Privadas")
            .setLore(Collections.singletonList(ChatColor.GRAY + "Clique para desativar as mensagem privadas")).build();

    public static ItemStack chatItem = new ItemManager(Material.BOOK, Util.color3 + "Chat")
            .setLore(Collections.singletonList(ChatColor.GRAY + "Ativar/desativar o chat")).build();

    public static ItemStack chatActive = new ItemManager(Material.SULPHUR, ChatColor.RED + "Chat")
            .setLore(Collections.singletonList(ChatColor.GRAY + "Clique para ativar o chat")).build();

    public static ItemStack chatDisable = new ItemManager(Material.GLOWSTONE_DUST, ChatColor.GREEN + "Chat")
            .setLore(Collections.singletonList(ChatColor.GRAY + "Clique para desativar o chat")).build();

    public static ItemStack cooldownItem = new ItemManager(Material.WATCH, Util.color3 + "Cooldown")
            .setLore(Arrays.asList(ChatColor.GRAY + "Alterne a visualização dos Cooldowns", ChatColor.GRAY + "entre Chat e ActionBar.", ChatColor.RED + "ActionBar apenas para 1.8!")).build();

    public static ItemStack cooldownChat = new ItemManager(Material.SULPHUR, ChatColor.RED + "Cooldown")
            .setLore(Collections.singletonList(ChatColor.GRAY + "Clique para alterar para Chat")).build();

    public static ItemStack cooldownActionBar = new ItemManager(Material.GLOWSTONE_DUST, ChatColor.GREEN + "Cooldown")
            .setLore(Collections.singletonList(ChatColor.GRAY + "Clique para alterar para ActionBar")).build();

    public static void openPrefsMenu(Player player) {
        // Tell, chat, cooldownType
        Inventory prefs = Bukkit.createInventory(null, 4 * 9, Util.title + Util.color2 + " Ajustes");

        Account account = AccountManager.getInstance().getOrCreateAccount(player);

        prefs.setItem(10, tellItem);
        prefs.setItem(12, chatItem);
        prefs.setItem(14, cooldownItem);

        if (account.isTell()) {
            prefs.setItem(19, tellDisable);
        } else {
            prefs.setItem(19, tellActive);
        }

        if (account.isChat()) {
            prefs.setItem(21, chatDisable);
        } else {
            prefs.setItem(21, chatActive);
        }

        if (account.getCooldownType() == CooldownType.CHAT) {
            prefs.setItem(23, cooldownActionBar);
        } else {
            prefs.setItem(23, cooldownChat);
        }

        player.openInventory(prefs);
    }
}
