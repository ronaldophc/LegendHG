package com.ronaldophc.listener;

import com.ronaldophc.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

import java.util.Arrays;
import java.util.List;

public class BlockNewVersionListener implements Listener {

    List<Material> blockedMaterials = Arrays.asList(Material.BARRIER, Material.PRISMARINE, Material.PRISMARINE_CRYSTALS,
            Material.PRISMARINE_SHARD, Material.SEA_LANTERN, Material.RED_SANDSTONE, Material.RED_SANDSTONE_STAIRS);

    // Bloquear colocação de blocos exclusivos da 1.8
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Material block = event.getBlockPlaced().getType();

        // Lista de blocos exclusivos da 1.8
        if (blockedMaterials.contains(block)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Util.error + "Blocos da 1.8 não é permitido no servidor!");
        }

    }

    // Bloquear entidades exclusivas da 1.8
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        EntityType entity = event.getEntityType();

        // Lista de entidades exclusivas da 1.8
        if (entity == EntityType.ARMOR_STAND || entity == EntityType.GUARDIAN || entity == EntityType.ENDERMITE ||
                entity == EntityType.RABBIT) {
            event.getEntity().remove();
            event.setCancelled(true);
        }
    }

    // Bloquear spawn de criaturas exclusivas da 1.8
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        EntityType entity = event.getEntityType();

        // Lista de criaturas exclusivas da 1.8
        if (entity == EntityType.GUARDIAN || entity == EntityType.ENDERMITE || entity == EntityType.RABBIT) {
            event.getEntity().remove();
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawn(ItemSpawnEvent event) {
        if (blockedMaterials.contains(event.getEntity().getItemStack().getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        // Verifica se o bloco afetado é uma esponja
        Block block = event.getBlock();
        if (block.getType() == Material.SPONGE) {
            // Cancela o evento de física, impedindo que a esponja absorva água
            event.setCancelled(true);
        }
    }
}
