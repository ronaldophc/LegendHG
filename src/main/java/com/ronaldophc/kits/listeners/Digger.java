package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.kits.manager.kits.gladiator.GladiatorController;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class Digger implements Listener {

    Kits DIGGER = Kits.DIGGER;

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        if (LegendHG.getGameStateManager().getGameState() == GameState.INVINCIBILITY) return;

        Player digger = event.getPlayer();
        KitManager kitManager = LegendHG.getKitManager();
        if (!kitManager.isThePlayerKit(digger, DIGGER)) return;
        if (!kitManager.isItemKit(digger.getItemInHand(), DIGGER)) return;
        event.setCancelled(true);

        GladiatorController gladiatorController = LegendHG.getGladiatorController();
        if (gladiatorController.isPlayerInFight(digger)) return;

        if (kitManager.isOnCooldown(digger, DIGGER)) return;
        kitManager.setCooldown(digger, 30, DIGGER);

        final Block b = event.getBlockPlaced();
        digger.getWorld().createExplosion(event.getBlockPlaced().getLocation(), 0F);

        int dist = 3;
        for (int y = 0; y >= -5; y--) {
            for (int x = -dist; x <= dist; x++) {
                for (int z = -dist; z <= dist; z++) {
                    if (b.getY() + y > 0) {
                        Block block = b.getWorld().getBlockAt(b.getX() + x, b.getY() + y, b.getZ() + z);
                        if (block.getType() != Material.BEDROCK) {
                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }
}
