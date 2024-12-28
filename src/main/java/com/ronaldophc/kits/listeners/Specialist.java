package com.ronaldophc.kits.listeners;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.kits.manager.kits.specialist.FakeEnchant;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;

public class Specialist implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        KitManager kitManager = LegendHG.getKitManager();
        if (event.getItem() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (!kitManager.isThePlayerKit(player, Kits.SPECIALIST)) return;
        if (!kitManager.isItemKit(player.getItemInHand(), Kits.SPECIALIST)) return;

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        int containerId = entityPlayer.nextContainerCounter();
        IChatBaseComponent title = new ChatMessage("Specialist", new Object[]{});

        FakeEnchant fakeEnchant = new FakeEnchant(entityPlayer.inventory, entityPlayer.world, new BlockPosition(0, 0, 0));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, "minecraft:enchanting_table", title));
        entityPlayer.activeContainer = fakeEnchant;
        entityPlayer.activeContainer.windowId = containerId;
        entityPlayer.activeContainer.addSlotListener(entityPlayer);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory == null) return;

        if (inventory.getType() != InventoryType.ENCHANTING) return;

        inventory.setItem(1, null);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        if (inventory == null) return;
        if (inventory.getType() != InventoryType.ENCHANTING) return;
        if (event.getSlot() == 1) event.setCancelled(true);
        if (event.getSlot() == 0) {
            inventory.setItem(1, new ItemStack(Material.INK_SACK, 3, (short) 4));
            player.updateInventory();
        }
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        Player player = event.getEntity().getKiller();
        KitManager kitManager = LegendHG.getKitManager();
        if (kitManager.isThePlayerKit(player, Kits.SPECIALIST)) {
            player.setLevel(player.getLevel() + 1);
        }
    }


    @EventHandler
    public void onPrepare(PrepareItemEnchantEvent event) {
        event.getExpLevelCostsOffered()[0] = 1;
        event.getExpLevelCostsOffered()[1] = 1;
        event.getExpLevelCostsOffered()[2] = 1;
    }

}
