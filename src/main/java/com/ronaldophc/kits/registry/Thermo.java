package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Thermo extends Kit {

    public Thermo() {
        super("Thermo",
                "legendhg.kits.thermo",
                new ItemManager(Material.DAYLIGHT_DETECTOR, Util.color3 + "Thermo")
                        .setLore(Collections.singletonList(Util.success + "Inverta Agua e Lava"))
                        .build(),
                new ItemManager(Material.DAYLIGHT_DETECTOR, Util.color3 + "Thermo")
                        .setUnbreakable()
                        .build(),
                false);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;

        ItemStack itemInHand = player.getItemInHand();
        if (!isItemKit(itemInHand)) return;
        if (kitManager.isOnCooldown(player, this)) return;

        Block blockClicked = event.getClickedBlock();
        if (blockClicked != null) {
            Set<Block> blocks = new HashSet<>();
            for (Block adjacentBlock : getAdjacentBlocks(blockClicked)) {
                int max = 150;
                if (adjacentBlock.getType() == Material.WATER || adjacentBlock.getType() == Material.STATIONARY_WATER) {
                    blocks.addAll(collectBlocks(adjacentBlock, Material.WATER, Material.STATIONARY_WATER));
                    if (blocks.size() > max) {
                        player.sendMessage("§cVocê não pode transformar mais de " + max + " blocos de água em lava.");
                        return;
                    }
                    transformBlocks(blocks, Material.LAVA);
                    kitManager.setCooldown(player, 3, this);
                    break;
                }
                if (adjacentBlock.getType() == Material.LAVA || adjacentBlock.getType() == Material.STATIONARY_LAVA) {
                    blocks.addAll(collectBlocks(adjacentBlock, Material.LAVA, Material.STATIONARY_LAVA));
                    if (blocks.size() > max) {
                        player.sendMessage("§cVocê não pode transformar mais de max " + max + " blocos de lava em água.");
                        return;
                    }
                    transformBlocks(blocks, Material.WATER);
                    kitManager.setCooldown(player, 3, this);
                    break;
                }
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
        for (Block waterBlock : blocks) {
            waterBlock.setType(Material.AIR);
        }
        for (Block waterBlock : blocks) {
            waterBlock.setType(type);
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