package com.ronaldophc.kits.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.kits.manager.kits.KangarooManager;

public class Kangaroo implements Listener {

    private final Kits KANGAROO = Kits.KANGAROO;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        KitManager kitManager = LegendHG.getKitManager();
        KangarooManager kangarooManager = KangarooManager.getInstance();

        Player kangaroo = event.getPlayer();
        if (!kitManager.isThePlayerKit(kangaroo, KANGAROO)) return;
        if (!kitManager.isItemKit(kangaroo.getItemInHand(), KANGAROO)) return;

        event.setCancelled(true);

        if (kitManager.isOnCooldown(kangaroo, KANGAROO)) return;
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

        KitManager kitManager = LegendHG.getKitManager();
        if (!(kitManager.isThePlayerKit(kangaroo, KANGAROO))) return;
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
