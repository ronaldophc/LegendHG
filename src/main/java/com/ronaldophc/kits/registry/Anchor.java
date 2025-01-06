package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.Collections;

public class Anchor extends Kit {

    public Anchor() {
        super("Anchor",
                "legendhg.kits.anchor",
                new ItemManager(Material.ANVIL, Util.color3 + "Anchor").setLore(Arrays.asList(Util.success + "Não tome knockback", Util.success + "ao tomar um hit.")).build(),
                null,
                false);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player damaged = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();
        Account damagedAccount = AccountManager.getInstance().getOrCreateAccount(damaged);
        Account damagerAccount = AccountManager.getInstance().getOrCreateAccount(damager);
        if (!damagedAccount.getKits().contains(this) && !damagerAccount.getKits().contains(this)) return;
        kitManager.setCombatLogCooldown(damaged, damager);
        damaged.damage(event.getDamage());
        event.setCancelled(true);
    }

}
