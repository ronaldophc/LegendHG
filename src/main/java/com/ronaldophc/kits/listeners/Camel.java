package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Camel implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player player = event.getPlayer();
        if (!LegendHG.getKitManager().isThePlayerKit(player, Kits.CAMEL)) return;
        if (player.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.SAND) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 3, 1));
        }
    }
}
