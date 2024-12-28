package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.constant.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.sql.SQLException;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) throws SQLException {
        if (LegendHG.getGameStateManager().getGameState() == GameState.COUNTDOWN) {
            if (!PlayerSQL.isPlayerLoggedIn(event.getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

}
