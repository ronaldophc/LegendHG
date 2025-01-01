package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Pyro extends Kit {

    public Pyro() {
        super("Pyro",
                "legendhg.kits.pyro",
                new ItemManager(Material.FIREBALL, Util.color3 + "Pyro")
                        .setLore(Arrays.asList(Util.success + "Lance uma bola de fogo", Util.success + "na direção que estiver olhando."))
                        .build(),
                Arrays.asList(new ItemStack[] {new ItemManager(Material.FIREBALL, Util.color3 + "Pyro")
                        .build()}),
                false);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Account account = AccountManager.getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;
        if (!isItemKit(event.getItem())) return;
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) {
            event.setCancelled(true);
            return;
        }
        if (!event.getAction().name().contains("RIGHT")) return;
        if (kitManager.isOnCooldown(player, this)) {
            event.setCancelled(true);
            return;
        }
        kitManager.setCooldown(player, 10, this);
        player.launchProjectile(Fireball.class);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Fireball)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Fireball fireball = (Fireball) event.getDamager();
        if (!(fireball.getShooter() instanceof Player)) return;

        Player player = (Player) fireball.getShooter();
        Player target = (Player) event.getEntity();

        Account account = AccountManager.getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;
        if (player == target) {
            event.setCancelled(true);
        }
    }
}
