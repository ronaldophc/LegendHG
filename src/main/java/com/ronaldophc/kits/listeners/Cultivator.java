package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class Cultivator implements Listener {

    Kits CULTIVATOR = Kits.CULTIVATOR;
    @EventHandler
    public void onPlant(BlockPlaceEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player cultivator = event.getPlayer();
        KitManager kitManager = LegendHG.getKitManager();
        if (!kitManager.isThePlayerKit(cultivator, CULTIVATOR)) return;

        Block eventBlock = event.getBlock();
        Material eventItem = event.getBlock().getType();
        if (eventItem == Material.SAPLING) {
            eventBlock.getWorld().generateTree(eventBlock.getLocation(), TreeType.TREE);
            eventBlock.setType(Material.LOG);
            return;
        }
        if (eventItem == Material.CROPS) {
            eventBlock.setData((byte)7);
            cultivator.sendMessage("Event item: " + eventItem);
            return;
        }
        if (eventItem == Material.COCOA) {
            eventBlock.setData((byte)8);
        }
    }
}
