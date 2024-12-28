package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class JackHammer implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player player = event.getPlayer();
        if (!LegendHG.getKitManager().isThePlayerKit(player, Kits.JACKHAMMER)) return;

        KitManager kitManager = LegendHG.getKitManager();
        if (!kitManager.isItemKit(player.getItemInHand(), Kits.JACKHAMMER)) return;

        Block block = event.getBlock();
        int y = block.getY();
        for (int i = 1; i < y + 1; i++) {
            Block destroyBlock = block.getWorld().getBlockAt(block.getX(), i, block.getZ());
            if (destroyBlock.getType().isSolid() && block.getType() != Material.BEDROCK) {
                destroyBlock.setType(Material.AIR);
            }
        }
    }
}
