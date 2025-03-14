package com.ronaldophc.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BlockBreak implements Listener {

    private final List<Material> instDrops = Arrays.asList(
            Material.BROWN_MUSHROOM,
            Material.RED_MUSHROOM,
            Material.LOG,
            Material.LOG_2,
            Material.COCOA,
            Material.STONE
    );


    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (account.isBuild()) return;

        if (!LegendHG.getGameStateManager().getGameState().canBreakBlocks()) {
            event.setCancelled(true);
            return;
        }

        Block block = event.getBlock();
        int x = block.getX();
        int z = block.getZ();

        // Verifica se o bloco está fora dos limites do mapa
        if (x <= -375 || x >= 375 || z <= -375 || z >= 375 && !player.isOp()) {
            event.setCancelled(true);
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            return;
        }
        Material blockType = block.getType();
        int data = block.getData() / 4;

        if (instDrops.contains(blockType)) {
            Collection<ItemStack> drops = block.getDrops();
            block.setType(Material.AIR);

            if (blockType == Material.COCOA) {
                switch (data) {
                    case 0:
                        player.getInventory().addItem(new ItemStack(351, 1, (short) 0, (byte) 3));
                        break;
                    case 1:
                        player.getInventory().addItem(new ItemStack(351, 2, (short) 0, (byte) 3));
                        break;
                    case 2:
                        player.getInventory().addItem(new ItemStack(351, 3, (short) 0, (byte) 3));
                        break;
                }
                player.updateInventory();
                return;
            }

            for (ItemStack drop : drops) {
                player.getInventory().addItem(drop);
            }

        }
    }

}
