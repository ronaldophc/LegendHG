package com.ronaldophc.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();

        if (event.getBlock().getY() > 128 && !player.isOp()) {
            event.setCancelled(true);
            player.sendMessage("§cVocê não pode construir acima de 128 blocos.");
            return;
        }

        Account account = AccountManager.getInstance().getOrCreateAccount(player);

        if (!account.isBuild() && !LegendHG.getGameStateManager().getGameState().canPlaceBlocks()) {
            event.setCancelled(true);
        }
        if (AccountManager.getInstance().getOrCreateAccount(player).isSpectator()) {
            event.setCancelled(true);
        }
    }

}
