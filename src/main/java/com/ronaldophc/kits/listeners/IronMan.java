package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.database.CurrentGameSQL;
import com.ronaldophc.constant.Kits;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class IronMan implements Listener {

    @EventHandler
    public void onKill(PlayerDeathEvent event) throws SQLException {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player player = event.getEntity();
        if (event.getEntity().getKiller() == null) return;
        Player killer = player.getKiller();
        if (!LegendHG.getKitManager().isThePlayerKit(killer, Kits.IRONMAN)) return;
        int kills = CurrentGameSQL.getCurrentGameKills(killer, LegendHG.getGameId());
        if (kills > 5) {
            killer.getInventory().addItem(new ItemStack(Material.IRON_INGOT));
        }
        killer.getInventory().addItem(new ItemStack(Material.IRON_INGOT));
    }
}
