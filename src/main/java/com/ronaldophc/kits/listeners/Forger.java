package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Forger implements Listener {

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player forger = (Player) event.getWhoClicked();
        KitManager kitManager = LegendHG.getKitManager();

        ItemStack currentItem = event.getCurrentItem();

        if (!kitManager.isThePlayerKit(forger, Kits.FORGER)) return;
        if (currentItem == null || currentItem.getType() == Material.AIR) return;

        Inventory playerInv = forger.getInventory();

        if (currentItem.getType() != Material.IRON_ORE) return;

        for (ItemStack items : playerInv.getContents()) {
            if (items == null || items.getType() != Material.COAL) continue;

            int coalInStack = items.getAmount();
            if (coalInStack >= currentItem.getAmount()) {
                items.setAmount(items.getAmount() - currentItem.getAmount());
                currentItem.setType(Material.IRON_INGOT);
                break;
            }
            if (coalInStack < currentItem.getAmount()) {
                currentItem.setAmount(currentItem.getAmount() - coalInStack);
                playerInv.removeItem(items);
                playerInv.addItem(new ItemStack(Material.IRON_INGOT, coalInStack));
            }
        }
        event.setCancelled(true);
        forger.updateInventory();
    }

}
