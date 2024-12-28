package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.player.PlayerAliveManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Thor implements Listener {

    public final Kits THOR = Kits.THOR;
    private final Set<Material> validBaseMaterials = new HashSet<>(Arrays.asList(Material.GRASS, Material.SAND, Material.STONE, Material.COBBLESTONE, Material.GRAVEL, Material.NETHERRACK));

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) return;

        Player thor = event.getPlayer();
        KitManager kitManager = LegendHG.getKitManager();

        if (!kitManager.isThePlayerKit(thor, THOR)) return;
        if (event.getItem() == null) return;
        if (!kitManager.isItemKit(event.getItem(), THOR)) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (kitManager.isOnCooldown(thor, THOR)) return;

        Block block = thor.getTargetBlock((Set<Material>) null, 13);

        if (block == null || block.getType() == Material.AIR) return;

        Location targetLocation = block.getLocation();
        targetLocation.getWorld().strikeLightningEffect(targetLocation);

        for (UUID target : PlayerAliveManager.getInstance().getPlayersAlive()) {
            Player player = LegendHG.getInstance().getServer().getPlayer(target);
            if (player == null) continue;
            if (player == thor) continue;
            if (player.getLocation().distance(targetLocation) > 3) continue;
            player.damage(5.0D);
        }

        if (validBaseMaterials.contains(block.getType())) {
            block.getLocation().add(0, 1, 0).getBlock().setType(Material.FIRE);
        }

        kitManager.setCooldown(thor, 6, THOR);
    }


}
