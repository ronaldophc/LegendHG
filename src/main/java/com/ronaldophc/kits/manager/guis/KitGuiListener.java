package com.ronaldophc.kits.manager.guis;

import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.kits.manager.KitManager;

public class KitGuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player.hasMetadata("kitGui")) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();

            if (item == null || item.getType() == Material.AIR) {
                return;
            }

            ItemStack buttonBack = new ItemManager(Material.DOUBLE_PLANT, "Voltar").setData((byte) 0).build();
            ItemStack buttonNext = new ItemManager(Material.DOUBLE_PLANT, "Próximo").setData((byte) 0).build();
            int page = Integer.parseInt(player.getMetadata("kitGui").get(0).asString());
            int whoKit = Integer.parseInt(player.getMetadata("whoKit").get(0).asString());

            if (item.equals(buttonBack)) {
                KitGui.openGui(player, page - 1, whoKit);
                return;
            }

            if (item.equals(buttonNext)) {
                KitGui.openGui(player, page + 1, whoKit);
                return;
            }

            KitManager kitManager = LegendHG.getKitManager();
            if (!kitManager.isItemIconKit(item)) return;

            try {
                String kitName = item.getItemMeta().getDisplayName();
                kitName = kitName.replace("§e", "");
                String command = "/kit2 " + kitName;
                if (whoKit == 1) {
                    command = "/kit " + kitName;
                }
                player.chat(command);
                player.closeInventory();
            } catch (Exception e) {
                Log.error("Função: onInventoryBlick - KitGuiListener.\nPlayer: " + player.getName() + ", kit: " + item.getItemMeta().getDisplayName());
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (player.hasMetadata("kitGui") || player.hasMetadata("whoKit")) {
            player.removeMetadata("kitGui", LegendHG.getInstance());
            player.removeMetadata("whoKit", LegendHG.getInstance());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().name().contains("RIGHT")) {
            ItemStack item = event.getItem();
            if (item == null || item.getType() == Material.AIR) return;
            if (item.getType() == Material.CHEST && item.getEnchantments().containsKey(Enchantment.DURABILITY)) {
                int whoKit = item.getEnchantmentLevel(Enchantment.DURABILITY);
                KitGui.openGui(player, 1, whoKit);
            }
        }
    }
}
