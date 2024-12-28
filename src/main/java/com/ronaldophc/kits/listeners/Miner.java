package com.ronaldophc.kits.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;

public class Miner implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player miner = event.getPlayer();
        if (!LegendHG.getKitManager().isThePlayerKit(miner, Kits.MINER)) return;

        if (miner.getItemInHand() == null || !LegendHG.getKitManager().isItemKit(miner.getItemInHand(), Kits.MINER))
            return;

        Block brokenBlock = event.getBlock();
        Material brokenBlockType = brokenBlock.getType();
        if (!brokenBlockType.name().contains("ORE")) return;

        breakAdjacentBlocks(brokenBlock, brokenBlockType, 4);
    }

    private void breakAdjacentBlocks(Block block, Material type, int depth) {
        if (depth <= 0) return;

        Vector[] directions = {
                new Vector(1, 0, 0),  // Direita
                new Vector(-1, 0, 0), // Esquerda
                new Vector(0, 1, 0),  // Cima
                new Vector(0, -1, 0), // Baixo
                new Vector(0, 0, 1),  // Frente
                new Vector(0, 0, -1)  // Trás
        };

        for (Vector direction : directions) {
            Block adjacentBlock = block.getRelative(direction.getBlockX(), direction.getBlockY(), direction.getBlockZ());
            if (adjacentBlock.getType() == type) {
                adjacentBlock.breakNaturally();
                breakAdjacentBlocks(adjacentBlock, type, depth - 1);
            }
        }
    }
}