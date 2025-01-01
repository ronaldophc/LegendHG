package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Fisherman extends Kit {

    public Fisherman() {
        super("Fisherman",
                "legendhg.kits.fisherman",
                new ItemManager(Material.FISHING_ROD, Util.color3 + "Fisherman")
                        .setLore(Arrays.asList(Util.success + "Ao acertar um jogador", Util.success + "puxará ele até você."))
                        .build(),
                Arrays.asList(new ItemStack[]{new ItemManager(Material.FISHING_ROD, Util.color3 + "Fisherman")
                        .setUnbreakable()
                        .build()}),
                false);

    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (!(event.getCaught() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player player = event.getPlayer();
        Account account = AccountManager.getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;

        Entity caught = event.getCaught();
        caught.teleport(player.getLocation());

        event.setCancelled(true);
    }
}