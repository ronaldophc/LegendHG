package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;

public class Boxer extends Kit {

    public Boxer() {
        super    ("Boxer",
                "legendhg.kits.boxer",
                new ItemManager(Material.GOLDEN_APPLE, Util.color3 + "Boxer")
                        .setLore(Arrays.asList(Util.success + "Leve menos dano", Util.success + "de outros jogadores"))
                        .build(),
                null,
                false);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player player = (Player) event.getEntity();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;
        event.setDamage(event.getDamage() - 0.5);
    }

    @Override
    public void apply(Player player) {
    }
}
