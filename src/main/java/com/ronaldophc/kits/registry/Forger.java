package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Forger extends Kit {

    public Forger() {
        super("Forger",
                "legendhg.kits.forger",
                new ItemManager(Material.COAL, Util.color3 + "Forger")
                        .setLore(Arrays.asList(Util.success + "Queime ferro", Util.success + "sem fornalha."))
                        .build(),
                null,
                false);
    }

    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player forger = (Player) event.getWhoClicked();

        ItemStack currentItem = event.getCurrentItem();

        Account account = AccountManager.getInstance().getOrCreateAccount(forger);
        if (!account.getKits().contains(this)) return;
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
