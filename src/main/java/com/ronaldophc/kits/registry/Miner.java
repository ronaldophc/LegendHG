package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class Miner extends Kit {

    public Miner() {
        super("Miner",
                "legendhg.kits.miner",
                new ItemManager(Material.STONE_PICKAXE, Util.color3 + "Miner")
                        .setLore(Arrays.asList(Util.success + "Ao acertar um bloco", Util.success + "ele será instantaneamente", Util.success + "quebrado."))
                        .build(),
                new ItemManager(Material.STONE_PICKAXE, Util.color3 + "Miner")
                        .setUnbreakable()
                        .addEnchantment(Enchantment.DIG_SPEED, 2)
                        .build(),
                false);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player miner = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(miner);
        if (!account.getKits().contains(this)) return;

        if (miner.getItemInHand() == null || !isItemKit(miner.getItemInHand()))
            return;

        Block brokenBlock = event.getBlock();
        Material brokenBlockType = brokenBlock.getType();
        if (!brokenBlockType.name().contains("ORE")) return;

        breakAdjacentBlocks(brokenBlock, brokenBlockType, 6);
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