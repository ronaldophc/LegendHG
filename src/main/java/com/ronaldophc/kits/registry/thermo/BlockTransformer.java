package com.ronaldophc.kits.registry.thermo;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BlockTransformer {

    private static final int MAX_BLOCKS = 150;

    public void transformBlocks(Player player, Block startBlock) {
        Set<Block> blocks = new HashSet<>();
        for (Block adjacentBlock : getAdjacentBlocks(startBlock)) {
            if (adjacentBlock.getType() == Material.WATER || adjacentBlock.getType() == Material.STATIONARY_WATER) {
                blocks.addAll(collectBlocks(adjacentBlock, Material.WATER, Material.STATIONARY_WATER));

                if (blocks.size() > MAX_BLOCKS) {
                    player.sendMessage("§cVocê não pode transformar mais de " + MAX_BLOCKS + " blocos de água em lava.");
                    return;
                }

                transformBlocks(blocks, Material.LAVA);
                return;
            }
            if (adjacentBlock.getType() == Material.LAVA || adjacentBlock.getType() == Material.STATIONARY_LAVA) {
                blocks.addAll(collectBlocks(adjacentBlock, Material.LAVA, Material.STATIONARY_LAVA));

                if (blocks.size() > MAX_BLOCKS) {
                    player.sendMessage("§cVocê não pode transformar mais de " + MAX_BLOCKS + " blocos de lava em água.");
                    return;
                }

                transformBlocks(blocks, Material.WATER);
                return;
            }
        }
    }

    private Set<Block> collectBlocks(Block startBlock, Material type1, Material type2) {
        Set<Block> blocks = new HashSet<>();
        Queue<Block> queue = new LinkedList<>();
        queue.add(startBlock);

        while (!queue.isEmpty()) {
            Block currentBlock = queue.poll();
            if ((currentBlock.getType() == type1 || currentBlock.getType() == type2) && !blocks.contains(currentBlock)) {
                blocks.add(currentBlock);

                for (Block adjacentBlock : getAdjacentBlocks(currentBlock)) {
                    if (!blocks.contains(adjacentBlock)) {
                        queue.add(adjacentBlock);
                    }
                }
            }
        }
        return blocks;
    }

    private void transformBlocks(Set<Block> blocks, Material type) {
        for (Block block : blocks) {
            block.setType(Material.AIR);
        }
        for (Block block : blocks) {
            block.setType(type);
        }
    }

    private Block[] getAdjacentBlocks(Block block) {
        return new Block[]{
                block.getRelative(1, 0, 0),
                block.getRelative(-1, 0, 0),
                block.getRelative(0, 1, 0),
                block.getRelative(0, -1, 0),
                block.getRelative(0, 0, 1),
                block.getRelative(0, 0, -1)
        };
    }
}
