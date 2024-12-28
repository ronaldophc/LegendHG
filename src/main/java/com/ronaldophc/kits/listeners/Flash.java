package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;

public class Flash implements Listener {

    private final Kits FLASH = Kits.FLASH;

    @EventHandler
    public void onFlash(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player flash = event.getPlayer();
        if (!LegendHG.getKitManager().isThePlayerKit(flash, FLASH)) return;

        KitManager kitManager = LegendHG.getKitManager();
        if (!kitManager.isItemKit(flash.getItemInHand(), FLASH)) return;
        if (kitManager.isOnCooldown(flash, FLASH)) return;

        Block block = flash.getTargetBlock((Set<Material>) null, 70);
        if (block == null || block.getType() == Material.AIR) return;

        Location targetLocation = block.getLocation().add(0, 1, 0);
        flash.teleport(targetLocation);
        flash.playSound(flash.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        kitManager.setCooldown(flash, 15, FLASH);
    }
}
