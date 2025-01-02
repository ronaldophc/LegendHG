package com.ronaldophc.kits;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@Getter
public abstract class Kit implements Listener {

    private final String name;
    private final String permission;
    private final ItemStack kitIcon;
    private final ItemStack kitItem;
    private final boolean combatLog;
    public static final KitManager kitManager = LegendHG.getKitManager();

    public Kit(String name, String permission, ItemStack kitIcon, ItemStack kitItem, boolean combatLog) {
        this.name = name;
        this.permission = permission;
        this.kitIcon = kitIcon;
        this.kitItem = kitItem;
        this.combatLog = combatLog;
    }

    public boolean isItemKit(ItemStack item) {
        if (kitItem == null) return false;
        return kitItem.isSimilar(item);
    }

    public void apply(Player player) {
        if (kitItem != null) {
            player.getInventory().addItem(kitItem);
            player.updateInventory();
        }
    }
}