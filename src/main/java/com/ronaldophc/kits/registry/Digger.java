package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.registry.gladiator.GladiatorController;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;

public class Digger extends Kit {


    public Digger() {
        super("Digger",
                "legendhg.kits.digger",
                new ItemManager(Material.DRAGON_EGG, Util.color3 + "Digger")
                        .setLore(Arrays.asList(Util.success + "Escave uma area", Util.success + "para baixo."))
                        .build(),
                new ItemManager(Material.DRAGON_EGG, Util.color3 + "Digger")
                        .setUnbreakable()
                        .build(),
                false);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        if (LegendHG.getGameStateManager().getGameState() == GameState.INVINCIBILITY) return;

        Player digger = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(digger);
        if (!account.getKits().contains(this)) return;
        if (!(isItemKit(event.getItemInHand()))) return;
        event.setCancelled(true);

        GladiatorController gladiatorController = LegendHG.getGladiatorController();
        if (gladiatorController.isPlayerInFight(digger)) return;

        if (kitManager.isOnCooldown(digger, this)) return;
        kitManager.setCooldown(digger, 30, this);

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
