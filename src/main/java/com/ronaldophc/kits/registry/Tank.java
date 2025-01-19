package com.ronaldophc.kits.registry;

import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Arrays;

public class Tank extends Kit {

    public Tank() {
        super("Tank",
                "legendhg.kits.tank",
                new ItemManager(Material.TNT, Util.color3 + "Tank")
                        .setLore(Arrays.asList(Util.success + "Ganhe resistência a explosão.", Util.success + "Crie uma explosão ao matar alguém."))
                        .build(),
                null,
                false);
    }

    @EventHandler
    public void onExplode(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player tank = (Player) event.getEntity();

        Account account = AccountManager.getInstance().getOrCreateAccount(tank);
        if (!account.getKits().contains(this)) return;

        if (event.getCause().name().contains("EXPLOSION")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }
        Player player = event.getEntity();
        Player tank = event.getEntity().getKiller();

        Account account = AccountManager.getInstance().getOrCreateAccount(tank);
        if (!account.getKits().contains(this)) return;

        event.getEntity().getWorld().createExplosion(player.getLocation(), 4.0F);
    }
}
