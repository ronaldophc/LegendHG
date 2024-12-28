package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Cannibal implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player player = event.getEntity();
        if (event.getEntity().getKiller() == null) return;
        Player killer = player.getKiller();
        if (!LegendHG.getKitManager().isThePlayerKit(killer, Kits.CANNIBAL)) return;
        killer.setHealth(Math.min(killer.getHealth() + 2, 20));
        killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 120, 1));
    }
}
