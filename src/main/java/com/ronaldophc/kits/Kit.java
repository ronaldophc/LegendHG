package com.ronaldophc.kits;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Kit implements Listener {
    private final String name;
    private final String permission;
    private final ItemStack kitIcon;
    private final List<ItemStack> kitItems;
    private final boolean combatLog;

    public Kit(String name, String permission, ItemStack kitIcon, List<ItemStack> kitItems, boolean combatLog) {
        this.name = name;
        this.permission = permission;
        this.kitIcon = kitIcon;
        this.kitItems = kitItems;
        this.combatLog = combatLog;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public ItemStack getKitIcon() {
        return kitIcon;
    }

    public List<ItemStack> getKitItems() {
        return kitItems;
    }

    public boolean isCombatLog() {
        return combatLog;
    }

    public abstract void apply(Player player);
}