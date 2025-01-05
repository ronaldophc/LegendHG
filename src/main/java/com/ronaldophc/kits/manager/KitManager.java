package com.ronaldophc.kits.manager;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.TitleHelper;
import com.ronaldophc.kits.CooldownAPI;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class KitManager {

    private final List<Kit> kits = new ArrayList<>();
    private final CooldownAPI cooldownAPI = CooldownAPI.getInstance();

    public void registerKit(Kit kit, Plugin plugin) {
        kits.add(kit);
        Bukkit.getPluginManager().registerEvents(kit, plugin);
    }

    public Kit[] getKits() {
        return kits.toArray(new Kit[kits.size()]);
    }

    public boolean isItemIconKit(ItemStack item) {
        return kits.stream().anyMatch(kit -> kit.getKitIcon().isSimilar(item));
    }

    public Kit searchKit(String name) {
        return kits.stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst()
                .orElse(null);
    }

    public boolean isOnCooldown(Player player, Kit kit) {
        if (cooldownAPI.isOnCooldown(player, kit)) {
            TitleHelper.sendCooldownBar(player, kit);
            return true;
        }
        if (cooldownAPI.isOnCombatLogCooldown(player) && kit.isCombatLog()) {
            TitleHelper.sendCombatLogCooldownBar(player);
            return true;
        }
        return false;
    }

    public int getCooldown(Player player, Kit kit) {
        return cooldownAPI.getCooldown(player, kit);
    }

    public int getCombatLogCooldown(Player player) {
        return cooldownAPI.getCombatLogCooldown(player);
    }

    public boolean isOnCombatLogCooldown(Player player) {
        return cooldownAPI.isOnCombatLogCooldown(player);
    }

    public Player getCombatLogHitterPlayer(Player player) {
        if (!isOnCombatLogCooldown(player)) return null;
        return Bukkit.getPlayer(cooldownAPI.getCombatLogHitter(player));
    }

    public void setCombatLogCooldown(Player player, Player hitter) {
        cooldownAPI.setCombatLogCooldown(player, hitter, 5);
    }

    public void setCooldown(Player player, int seconds, Kit kit) {
        cooldownAPI.setCooldown(player, kit, seconds);
    }
}