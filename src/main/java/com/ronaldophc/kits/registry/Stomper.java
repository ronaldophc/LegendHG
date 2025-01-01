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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Stomper extends Kit {

    public Stomper() {
        super("Stomper",
                "legendhg.kits.stomper",
                new ItemManager(Material.IRON_BOOTS, Util.color3 + "Stomper")
                        .setLore(Arrays.asList(Util.success + "Ao cair de uma altura", Util.success + "causará dano em jogadores", Util.success + "próximos."))
                        .build(),
                Collections.emptyList(),
                false);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(LegendHG.getGameStateManager().getGameState().canTakeDamage())) return;
        if (!(LegendHG.getGameStateManager().getGameState().canUseKit())) return;
        if (event.getEntity() == null) {
            System.err.println("Player is null in Stomper.onPlayerDamage");
            return;
        }
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        Account account = AccountManager.getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;

        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause != EntityDamageEvent.DamageCause.FALL) return;

        if (event.getFinalDamage() <= 6) return;

        for (Entity entity : player.getNearbyEntities(5, 3, 5)) {
            if (entity instanceof Player) {
                Player target = (Player) entity;
                if (!target.isSneaking()) {
                    target.damage(event.getFinalDamage(), player);
                }
            }
        }

        event.setDamage(6);
    }
}
