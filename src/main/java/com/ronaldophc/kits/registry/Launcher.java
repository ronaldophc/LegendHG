package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import com.ronaldophc.kits.Kit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Iterator;

public class Launcher extends Kit {

    public Launcher() {
        super("Launcher",
                "legendhg.kits.launcher",
                new ItemManager(Material.SPONGE, Util.color3 + "Launcher")
                        .setLore(Arrays.asList(Util.success + "Ganhe impulsao", Util.success + "ao subir na esponja."))
                        .build(),
                new ItemManager(Material.SPONGE, Util.color3 + "Launcher")
                        .setAmount(20)
                        .build(),
                false);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

        if (!(block.hasMetadata("launcher"))) return;

        double strength = 3D;

        BlockFace face = (BlockFace) block.getMetadata("launcher").get(0).value();
        double y = face.getModY() * strength;

        if (y == 0) {
            y = 0.1D * strength;
        }

        y *= 1.5D;
        Vector vector = new Vector(face.getModX() * strength, y, face.getModZ() * strength);

        player.setFallDistance(-1000);
        player.setVelocity(vector);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!block.hasMetadata("launcher")) return;

        event.setCancelled(true);
        block.setType(Material.AIR);
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemManager(Material.SPONGE, Util.color3 + "Launcher").setAmount(1).build());
        block.removeMetadata("launcher", LegendHG.getInstance());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        ItemStack item = event.getItemInHand();

        if (item == null) return;
        if (!item.hasItemMeta()) return;
        if (!item.getItemMeta().hasDisplayName()) return;
        if (!item.getItemMeta().getDisplayName().equals(Util.color3 + "Launcher"))
            return;

        BlockFace face = event.getBlockAgainst().getFace(event.getBlock());
        if (face == BlockFace.DOWN) {
            face = BlockFace.UP;
        }
        block.setMetadata("launcher", new FixedMetadataValue(LegendHG.getInstance(), face));
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        Iterator<Block> itel = event.blockList().iterator();
        while (itel.hasNext()) {
            Block block = itel.next();
            if (block.hasMetadata("Launcher")) {
                BlockBreakEvent newEvent = new BlockBreakEvent(block, null);
                onBreak(newEvent);
                itel.remove();
            }
        }
    }

    @EventHandler
    public void onPiston(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (block.hasMetadata("launcher"))
                event.setCancelled(true);
        }
    }
}
