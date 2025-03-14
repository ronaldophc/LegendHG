package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Arrays;

public class IronMan extends Kit {

    public IronMan() {
        super("Ironman",
                "legendhg.kits.ironman",
                new ItemManager(Material.IRON_INGOT, Util.color3 + "Ironman")
                        .setLore(Arrays.asList(Util.success + "Ganhe iron a cada kill.", Util.success + "Ganhe 2 iron a partir de 5 kills."))
                        .build(),
                null,
                false);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) throws SQLException {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player player = event.getEntity();
        if (player.getKiller() == null) return;

        Player killer = player.getKiller();
        Account account = AccountManager.getInstance().getOrCreateAccount(killer);
        if (!account.getKits().contains(this)) return;

        int kills = account.getKills();
        if (kills > 5) {
            killer.getInventory().addItem(new ItemStack(Material.IRON_INGOT));
        }
        killer.getInventory().addItem(new ItemStack(Material.IRON_INGOT));
    }
}
