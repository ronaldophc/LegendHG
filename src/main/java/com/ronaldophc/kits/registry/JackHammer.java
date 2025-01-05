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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class JackHammer extends Kit {

    public JackHammer() {
        super("Jackhammer",
                "legendhg.kits.jackhammer",
                new ItemManager(Material.STONE_AXE, Util.color3 + "Jackhammer")
                        .setLore(Arrays.asList(Util.success + "Ao quebrar um bloco com o machado", Util.success + "quebre tudo até a bedrock"))
                        .setUnbreakable()
                        .build(),
                new ItemManager(Material.STONE_AXE, Util.color3 + "Jackhammer")
                        .setUnbreakable()
                        .build(),
                false);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player player = event.getPlayer();
        Account account = LegendHG.getAccountManager().getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;

        if (!isItemKit(player.getItemInHand())) return;

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
