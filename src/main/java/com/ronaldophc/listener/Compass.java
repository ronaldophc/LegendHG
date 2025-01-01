package com.ronaldophc.listener;

import com.ronaldophc.helper.Util;
import com.ronaldophc.player.PlayerAliveManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class Compass implements Listener {

    @EventHandler
    public void onInteractCompass(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.COMPASS) return;
        if (event.getAction() != Action.PHYSICAL) {
            Player target = getTarget(event.getPlayer());
            Player player = event.getPlayer();
            if (target == null) {
                player.sendMessage(Util.color1 + "Nenhum jogador foi encontrado.");
                return;
            }
            player.sendMessage(Util.color3 + "Bússola apontando para " + target.getName());
            player.setCompassTarget(target.getLocation());
        }
    }

    private Player getTarget(Player player) {
        Player target = null;
        for (UUID uuid : PlayerAliveManager.getInstance().getPlayersAlive()) {
            if (!PlayerAliveManager.getInstance().isPlayerOnline(uuid)) return null;
            try {
                Player playerTarget = Bukkit.getPlayer(uuid);
                if (playerTarget == player) continue;
                if (playerTarget.getLocation().distance(player.getLocation()) < 15.0D) continue;
                if (target == null) {
                    target = playerTarget;
                    continue;
                }
                double actualDistance = target.getLocation().distance(player.getLocation());
                double newDistance = playerTarget.getLocation().distance(player.getLocation());
                if (actualDistance > newDistance) target = playerTarget;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return target;
    }
}
