package com.ronaldophc.feature.battleonthesummit;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SummitListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (SummitManager.getInstance().getAccounts().contains(account)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (SummitManager.getInstance().getAccounts().contains(account)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!SummitManager.getInstance().getAccounts().contains(account)) {
            return;
        }
        if (player.getLocation().getY() < 20) {
            SummitManager.getInstance().playerLose(player);
        }
    }

    // ------------- KANGAROO ---------------- //

    private final List<Player> cooldown = new ArrayList<>();

    @EventHandler
    public void onInteractKangaroo(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);

        if (!SummitManager.getInstance().getAccounts().contains(account)) {
            return;
        }

        KitManager kitManager = LegendHG.getKitManager();

        if (event.getItem() == null) return;
        if (!(event.getItem().isSimilar(SummitManager.getInstance().kangarooItem))) return;

        event.setCancelled(true);

        if (event.getAction() == Action.PHYSICAL) return;

        if (player.isOnGround()) {
            if (!player.isSneaking()) {
                Vector vector = player.getEyeLocation().getDirection();
                vector.multiply(0.2F * ((15.0D) / 10.0D));
                vector.setY(0.65F * ((15.0D) / 10.0D));
                player.setVelocity(vector);
                cooldown.remove(player);
                return;
            }

            Vector vector = player.getEyeLocation().getDirection();
            vector.multiply(0.2F * ((20.0D) / 10.0D));
            vector.setY(0.45F * (1.0));
            player.setVelocity(vector);
            cooldown.remove(player);
            return;
        }
        if (!cooldown.contains(player)) {
            if (!player.isSneaking()) {
                Vector vector = player.getEyeLocation().getDirection();
                vector.multiply(0.2F * ((15.0D) / 10.0D));
                vector.setY(0.60F * ((15.0D) / 10.0D));
                player.setVelocity(vector);
                cooldown.add(player);
                return;
            }

            Vector vector = player.getEyeLocation().getDirection();
            vector.multiply(0.70F * ((20.0D) / 10.0D));
            vector.setY(0.45F * (1.0));
            player.setVelocity(vector);
            cooldown.add(player);
        }

    }

    @EventHandler
    public void onMoveKangaroo(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);

        if (!SummitManager.getInstance().getAccounts().contains(account)) {
            return;
        }

        if (!cooldown.contains(player)) return;
        if (!player.isOnGround()) return;

        cooldown.remove(player);
    }

    @EventHandler
    public void onDamageKangaroo(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);

        if (!SummitManager.getInstance().getAccounts().contains(account)) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        event.setDamage(0.0D);
    }

    // ---------------- SWITCHER ------------------ //

    @EventHandler
    public void onInteractSwitcher(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!SummitManager.getInstance().getAccounts().contains(account)) {
            return;
        }

        if (event.getItem() == null) return;

        if (!(event.getItem().isSimilar(SummitManager.getInstance().switcherItem))) return;
        if (!event.getAction().name().contains("RIGHT")) return;

        KitManager kitManager = LegendHG.getKitManager();
        Kit switcher = kitManager.searchKit("Switcher");
        if (kitManager.isOnCooldown(player, switcher)) {
            event.setCancelled(true);
            return;
        }
        kitManager.setCooldown(player, 1, switcher);
        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setMetadata("Switcher", new FixedMetadataValue(LegendHG.getInstance(),
                player.getUniqueId()));
        snowball.setShooter(player);
        snowball.setVelocity(snowball.getVelocity().multiply(2));
        event.setCancelled(true);
        player.updateInventory();
    }

    @EventHandler
    public void onDamageSwitcher(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Snowball)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Snowball snowball = (Snowball) event.getDamager();
        if (!(snowball.getShooter() instanceof Player)) return;

        Player player = (Player) snowball.getShooter();
        Player target = (Player) event.getEntity();

        KitManager kitManager = LegendHG.getKitManager();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);

        if (!SummitManager.getInstance().getAccounts().contains(account)) {
            return;
        }
        if (player == target) return;

        Location playerLocation = player.getLocation();
        Location targetLocation = target.getLocation();
        player.teleport(targetLocation);
        target.teleport(playerLocation);
        kitManager.setCooldown(player, 3, kitManager.searchKit("Switcher"));
    }
}
