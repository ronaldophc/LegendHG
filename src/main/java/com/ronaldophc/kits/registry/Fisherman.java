package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Arrays;

public class Fisherman extends Kit {

    public Fisherman() {
        super("Fisherman",
                "legendhg.kits.fisherman",
                new ItemManager(Material.FISHING_ROD, Util.color3 + "Fisherman")
                        .setLore(Arrays.asList(Util.success + "Ao acertar um jogador", Util.success + "puxará ele até você."))
                        .build(),
                new ItemManager(Material.FISHING_ROD, Util.color3 + "Fisherman")
                        .setUnbreakable()
                        .build(),
                false);

    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (!(event.getCaught() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;

        Entity caught = event.getCaught();
        caught.teleport(player.getLocation());

        event.setCancelled(true);
    }
}
