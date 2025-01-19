package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

public class Switcher extends Kit {

    public Switcher() {
        super("Switcher",
                "legendhg.kits.switcher",
                new ItemManager(Material.SNOW_BALL, Util.color3 + "Switcher")
                        .setLore(Arrays.asList(Util.success + "Troque de lugar com", Util.success + "o jogador que acertar."))
                        .build(),
                new ItemManager(Material.SNOW_BALL, Util.color3 + "Switcher")
                        .build(),
                false);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Account account = AccountManager.getInstance().getOrCreateAccount(player);
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
        kitManager.setCooldown(player, 1, this);
        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setMetadata("Switcher", new FixedMetadataValue(LegendHG.getInstance(),
                player.getUniqueId()));
        snowball.setShooter(player);
        snowball.setVelocity(snowball.getVelocity().multiply(2));
        event.setCancelled(true);
        player.updateInventory();
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Snowball)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Snowball snowball = (Snowball) event.getDamager();
        if (!(snowball.getShooter() instanceof Player)) return;

        Player player = (Player) snowball.getShooter();
        Player target = (Player) event.getEntity();

        KitManager kitManager = LegendHG.getKitManager();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);

        if (!account.getKits().contains(this)) return;
        if (player == target) return;

        Location playerLocation = player.getLocation();
        Location targetLocation = target.getLocation();
        player.teleport(targetLocation);
        target.teleport(playerLocation);
        kitManager.setCooldown(player, 3, this);
    }
}
