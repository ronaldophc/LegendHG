package com.ronaldophc.kits;

import com.ronaldophc.LegendHG;
import com.ronaldophc.gamestate.GameStateManager;
import com.ronaldophc.kits.manager.KitManager;
import org.bukkit.Material;
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
    public static final KitManager kitManager = LegendHG.getKitManager();

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

    public boolean isItemKit(ItemStack item) {
        return kitItems.contains(item);
    }

    public void apply(Player player) {
        getKitItems().forEach(itemStack -> player.getInventory().addItem(itemStack));
        player.updateInventory();
        if (player.getInventory().contains(Material.COMPASS)) return;
        player.getInventory().addItem(new ItemStack(Material.COMPASS));
        player.updateInventory();
    }
}