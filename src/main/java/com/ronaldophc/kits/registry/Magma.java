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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Magma extends Kit {

    public Magma() {
        super("Magma",
                "legendhg.kits.magma",
                new ItemManager(Material.MAGMA_CREAM, Util.color3 + "Magma")
                        .setLore(Arrays.asList(Util.success + "Ao bater em um jogador", Util.success + "tenha chance de aplicar fogo nele.", Util.success + "Não recebe dano de fogo."))
                        .build(),
                Collections.emptyList(),
                false);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player damager = (Player) event.getDamager();
        Account account = AccountManager.getOrCreateAccount(damager);
        if (!account.getKits().contains(this)) return;

        Player damaged = (Player) event.getEntity();
        Random random = new Random();
        int chance = random.nextInt(100);
        if (chance < 33) {
            damaged.setFireTicks(100);
        }
    }

    @EventHandler
    public void onFireTick(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player damaged = (Player) event.getEntity();
        Account account = AccountManager.getOrCreateAccount(damaged);
        if (!account.getKits().contains(this)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            event.setCancelled(true);
        }
    }
}
