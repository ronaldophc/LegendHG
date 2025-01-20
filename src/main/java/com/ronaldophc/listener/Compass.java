package com.ronaldophc.listener;

import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Compass implements Listener {

    @EventHandler
    public void onInteractCompass(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.COMPASS) return;
        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!account.isAlive() || account.isVanish()) return;
        if (event.getAction() != Action.PHYSICAL) {
            Player target = getTarget(event.getPlayer());
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
        for (Player playerTarget : AccountManager.getInstance().getPlayersAlive()) {

            if (playerTarget == player) continue;
            if (playerTarget.getLocation().distance(player.getLocation()) < 15.0D) continue;

            if (target == null) {
                target = playerTarget;
                continue;
            }

            double actualDistance = target.getLocation().distance(player.getLocation());
            double newDistance = playerTarget.getLocation().distance(player.getLocation());
            if (actualDistance > newDistance) {
                target = playerTarget;
            }
        }
        return target;
    }
}
