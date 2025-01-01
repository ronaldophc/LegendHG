package com.ronaldophc.kits.registry.gravity;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Gravity extends Kit {

    public Gravity() {
        super("Gravity",
                "legendhg.kits.gravity",
                new ItemManager(Material.SEA_LANTERN, Util.color3 + "Gravity")
                        .setLore(Arrays.asList(Util.success + "Ao acertar um jogador", Util.success + "ele será jogado para cima."))
                        .build(),
                Arrays.asList(new ItemStack[]{new ItemManager(Material.SEA_LANTERN, Util.color3 + "Gravity")
                        .setUnbreakable()
                        .build()}),
                false);
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (!(entity instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) return;

        Account account = AccountManager.getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;

        if (!isItemKit(player.getItemInHand())) return;

        if (kitManager.isOnCooldown(player, this)) return;
        kitManager.setCooldown(player, 15, this);

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
