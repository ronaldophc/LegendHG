package com.ronaldophc.kits.registry.kangaroo;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Kangaroo extends Kit {

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
        KangarooManager kangarooManager = KangarooManager.getInstance();

        Player kangaroo = event.getPlayer();

        Account account = LegendHG.getAccountManager().getOrCreateAccount(kangaroo);
        if (!account.getKits().contains(this)) return;

        if (!isItemKit(kangaroo.getItemInHand())) return;

        event.setCancelled(true);

        if (kitManager.isOnCooldown(kangaroo, this)) return;
        if (kangarooManager.hasPlayer(kangaroo) && kangarooManager.getPlayer(kangaroo) == 1) return;

        if (event.getAction() == Action.PHYSICAL) return;
        event.setCancelled(true);

        if (!kangaroo.isSneaking()) {
            Vector vector = kangaroo.getEyeLocation().getDirection().multiply(0.6F).setY(1.0F);
            kangaroo.setVelocity(vector);
        }

        if (kangaroo.isSneaking()) {
            kangaroo.setVelocity(kangaroo.getLocation().getDirection().multiply(1.2D));
            kangaroo.setVelocity(new Vector(kangaroo.getVelocity().getX(), 0.5D, kangaroo.getVelocity().getZ()));
        }

        if (!kangarooManager.hasPlayer(kangaroo)) {
            kangarooManager.setPlayer(kangaroo, 0);
            return;
        }

        kangarooManager.setPlayer(kangaroo, 1);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        KangarooManager kangarooManager = KangarooManager.getInstance();

        if (kangarooManager.hasPlayer(player)) {
            Block b = player.getLocation().getBlock();
            if (b.getType() != Material.AIR || b.getRelative(BlockFace.DOWN).getType() != Material.AIR && !player.isFlying()) {
                kangarooManager.removePlayer(player);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player kangaroo = (Player) event.getEntity();

        Account account = LegendHG.getAccountManager().getOrCreateAccount(kangaroo);
        if (!account.getKits().contains(this)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (event.getFinalDamage() < 6.0D) {
                event.setCancelled(true);
            }
            if (event.getFinalDamage() > 10.0D) {
                event.setDamage(10.0D);
            }
        }
    }
}
