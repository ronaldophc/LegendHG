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
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Fireman extends Kit {

    public Fireman() {
        super("Fireman",
                "legendhg.kits.fireman",
                new ItemManager(Material.LAVA_BUCKET, Util.color3 + "Fireman")
                        .setLore(Arrays.asList(Util.success + "Não tome dano", Util.success + "de lava e fogo.", Util.success + "E ganhe um balde de agua."))
                        .build(),
                Arrays.asList(new ItemStack[]{new ItemStack(Material.WATER_BUCKET)}),
                false);
    }

    @EventHandler
    public void onFireman(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player fireman = (Player) event.getEntity();
        Account account = AccountManager.getOrCreateAccount(fireman);
        if (!account.getKits().contains(this)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            event.setCancelled(true);
        }
    }
}