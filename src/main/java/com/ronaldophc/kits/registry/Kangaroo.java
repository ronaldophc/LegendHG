package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kangaroo extends Kit {

    private final List<Player> cooldown = new ArrayList<>();

    public Kangaroo() {
        super("Kangaroo",
                "legendhg.kits.kangaroo",
                new ItemManager(Material.FIREWORK, Util.color3 + "Kangaroo")
                        .setLore(Collections.singletonList(Util.success + "Pule como um canguru"))
                        .build(),
                new ItemManager(Material.FIREWORK, Util.color3 + "Kangaroo")
                        .setUnbreakable()
                        .build(),
                true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        KitManager kitManager = LegendHG.getKitManager();
        Player kangaroo = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(kangaroo);

        if (!account.getKits().contains(this)) return;

        if (!isItemKit(kangaroo.getItemInHand())) return;

        event.setCancelled(true);

        if (kitManager.isOnCooldown(kangaroo, this)) return;

        if (event.getAction() == Action.PHYSICAL) return;

        if (kangaroo.isOnGround()) {
            if (!kangaroo.isSneaking()) {
                Vector vector = kangaroo.getEyeLocation().getDirection();
                vector.multiply(0.3F * ((15.0D) / 10.0D));
                vector.setY(0.75F * ((15.0D) / 10.0D));
                kangaroo.setVelocity(vector);
                cooldown.remove(kangaroo);
                return;
            }

            Vector vector = kangaroo.getEyeLocation().getDirection();
            vector.multiply(0.3F * ((20.0D) / 10.0D));
            vector.setY(0.55F * (1.0));
            kangaroo.setVelocity(vector);
            cooldown.remove(kangaroo);
            return;
        }
        if (!cooldown.contains(kangaroo)) {
            if (!kangaroo.isSneaking()) {
                Vector vector = kangaroo.getEyeLocation().getDirection();
                vector.multiply(0.3F * ((15.0D) / 10.0D));
                vector.setY(0.70F * ((15.0D) / 10.0D));
                kangaroo.setVelocity(vector);
                cooldown.add(kangaroo);
                return;
            }

            Vector vector = kangaroo.getEyeLocation().getDirection();
            vector.multiply(0.80F * ((20.0D) / 10.0D));
            vector.setY(0.55F * (1.0));
            kangaroo.setVelocity(vector);
            cooldown.add(kangaroo);
        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player kangaroo = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(kangaroo);

        if (!account.getKits().contains(this)) return;
        if (!cooldown.contains(kangaroo)) return;
        if (!kangaroo.isOnGround()) return;

        cooldown.remove(kangaroo);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player kangaroo = (Player) event.getEntity();
        Account account = AccountManager.getInstance().getOrCreateAccount(kangaroo);

        if (!account.getKits().contains(this)) return;

        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (event.getFinalDamage() > 12.0D) {
            event.setDamage(12.0D);
        }
    }
}
