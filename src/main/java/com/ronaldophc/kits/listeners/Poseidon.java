package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Poseidon implements Listener {

    public int potionMultiplier = 0;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player player = event.getPlayer();
        if (!(LegendHG.getKitManager().isThePlayerKit(player, Kits.POSEIDON))) return;
        if (player.getRemainingAir() < 200) {
            player.setRemainingAir(200);
        }
        if (player.getLocation().getBlock().isLiquid()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, this.potionMultiplier), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, this.potionMultiplier), true);
        }
    }

}
