package com.ronaldophc.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class Soup implements Listener {

    @EventHandler
    public void onSoup(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null) return;
        if (itemInHand.getType() == Material.MUSHROOM_SOUP &&
                (event.getAction().name().contains("RIGHT")) &&
                player.getHealth() < player.getMaxHealth()) {

            player.setHealth(Math.min(player.getHealth() + 7, player.getMaxHealth()));
            player.setItemInHand(new ItemStack(Material.BOWL));
            player.updateInventory();
            event.setCancelled(true);
        }
    }

}