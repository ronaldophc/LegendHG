package com.ronaldophc.listener;

import com.ronaldophc.player.PlayerSpectatorManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.ronaldophc.LegendHG;

public class EntityEvents implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {

        if(!LegendHG.getGameStateManager().getGameState().canTakeDamage()) {
            event.setCancelled(true);
            return;
        }

        Entity entity = event.getEntity();

        // Eventos em que a entidade que tomou dano é um jogador
        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;
        if (PlayerSpectatorManager.getInstance().isPlayerSpectating(player)) {
            event.setCancelled(true);
        }

    }
}
