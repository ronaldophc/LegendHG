package com.ronaldophc.kits.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.kits.manager.kits.BlinkManager;

public class Blink implements Listener {

    private final Kits BLINK = Kits.BLINK;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player blinker = event.getPlayer();

        KitManager kitManager = LegendHG.getKitManager();

        if (!kitManager.isThePlayerKit(blinker, BLINK)) return;
        if (!kitManager.isItemKit(event.getItem(), BLINK)) return;

        if (kitManager.isOnCooldown(blinker, BLINK)) return;

        BlinkManager blinkManager = BlinkManager.getInstance();

        Block block = blinker.getEyeLocation().add(blinker.getEyeLocation().getDirection().multiply(5.0D)).getBlock();
        if (block.getY() > 130) {
            blinker.sendMessage(Util.errorServer + "Você não pode usar o blink acima de 130 blocos.");
            return;
        }

        int times = 0;

        if (blinkManager.isSetBlinkPlayer(blinker)) {
            times = blinkManager.getTimes(blinker);
        }

        if (block.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
            block.getRelative(BlockFace.DOWN).setType(Material.LEAVES);
        }

        blinker.teleport(new Location(blinker.getWorld(), block.getX(), block.getY(), block.getZ(), blinker.getLocation().getYaw(), blinker.getLocation().getPitch()));
        blinker.setFallDistance(0.0F);
        blinker.playSound(blinker.getLocation(), Sound.FIREWORK_LAUNCH, 1.0F, 50.0F);

        times++;
        if (times >= 3) {
            kitManager.setCooldown(blinker, 25, BLINK);
            blinkManager.setBlinkPlayer(blinker, 0);
            return;
        }

        blinkManager.setBlinkPlayer(blinker, times);
    }
}
