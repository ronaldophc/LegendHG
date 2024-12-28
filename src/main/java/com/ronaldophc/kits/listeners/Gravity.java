package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.kits.manager.kits.GravityManager;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Gravity implements Listener {

    Kits GRAVITY = Kits.GRAVITY;

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        KitManager kitManager = LegendHG.getKitManager();

        if (!(entity instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) return;
        if (!kitManager.isThePlayerKit(player, Kits.GRAVITY)) return;
        if (!kitManager.isItemKit(player.getItemInHand(), Kits.GRAVITY)) return;

        if (kitManager.isOnCooldown(player, Kits.GRAVITY)) return;
        kitManager.setCooldown(player, 15, Kits.GRAVITY);

        GravityManager gravityManager = GravityManager.getInstance();

        Player target = (Player) entity;
        if (gravityManager.hasPlayer(target)) return;

        target.setVelocity(target.getLocation().getDirection().setY(1.0D));
        Bukkit.getScheduler().scheduleSyncDelayedTask(LegendHG.getInstance(), () -> initGravity(target), 20L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(LegendHG.getInstance(), () -> removeGravity(target), 70L);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        GravityManager gravityManager = GravityManager.getInstance();

        if (gravityManager.hasPlayer(player)) {
            event.getTo().setY(event.getFrom().getY());
        }
    }

    public void initGravity(Player target) {
        GravityManager gravityManager = GravityManager.getInstance();
        gravityManager.addPlayer(target);

        target.getWorld().playEffect(target.getLocation(), Effect.SMOKE, 2);

        target.getWorld().playSound(target.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }

    public void removeGravity(Player target) {
        GravityManager gravityManager = GravityManager.getInstance();
        gravityManager.removePlayer(target);
        target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 80, 0));
    }
}
