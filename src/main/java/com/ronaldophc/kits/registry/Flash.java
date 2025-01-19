package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;
import java.util.Set;

public class Flash extends Kit {

    public Flash() {
        super("Flash",
                "legendhg.kits.flash",
                new ItemManager(Material.REDSTONE_TORCH_ON, Util.color3 + "Flash")
                        .setLore(Collections.singletonList(Util.success + "Corra na velocidade da luz"))
                        .build(),
                new ItemManager(Material.REDSTONE_TORCH_ON, Util.color3 + "Flash")
                        .setUnbreakable()
                        .build(),
                true);
    }

    @EventHandler
    public void onFlash(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player flash = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(flash);
        if (!account.getKits().contains(this)) return;

        if (!isItemKit(flash.getItemInHand())) return;
        if (kitManager.isOnCooldown(flash, this)) return;

        Block block = flash.getTargetBlock((Set<Material>) null, 70);
        if (block == null || block.getType() == Material.AIR) return;

        Location targetLocation = block.getLocation().add(0, 1, 0);
        flash.teleport(targetLocation);
        flash.playSound(flash.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        kitManager.setCooldown(flash, 15, this);
    }
}
