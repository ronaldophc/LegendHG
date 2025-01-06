package com.ronaldophc.kits.registry.ninja;

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
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.Arrays;

public class Ninja extends Kit {

    public Ninja() {
        super("Ninja",
                "legendhg.kits.ninja",
                new ItemManager(Material.NETHER_STAR, Util.color3 + "Ninja")
                        .setLore(Arrays.asList(Util.success + "Ao apertar shift", Util.success + "você será teleportado", Util.success + "para o ultimo jogador que bateu."))
                        .build(),
                null,
                false);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) return;

        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;

        Player ninja = (Player) event.getDamager();

        Account account = AccountManager.getInstance().getOrCreateAccount(ninja);
        if (!account.getKits().contains(this)) return;

        Player target = (Player) event.getEntity();

        NinjaManager ninjaManager = NinjaManager.getInstance();

        ninjaManager.pull(ninja, target);

    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player ninja = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(ninja);
        if (!account.getKits().contains(this)) return;

        NinjaManager ninjaManager = NinjaManager.getInstance();

        if (kitManager.isOnCooldown(ninja, this)) return;

        if (!ninjaManager.hasTarget(ninja)) return;
        if (!ninjaManager.isTargetOnline(ninja)) return;
        Player target = ninjaManager.getTarget(ninja);

        if (target.getLocation().distance(ninja.getLocation()) > 15D) {
            ninja.sendMessage(Util.errorServer + "Você está muito longe do seu alvo.");
            return;
        }

        ninja.teleport(target.getLocation());
        kitManager.setCooldown(ninja, 5, this);
        ninjaManager.removeTarget(ninja);
    }
}
