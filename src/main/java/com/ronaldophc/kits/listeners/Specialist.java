package com.ronaldophc.kits.listeners;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EnchantingInventory;
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
import org.bukkit.material.Dye;

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

        FakeEnchant fakeEnchant = new FakeEnchant(entityPlayer.inventory, entityPlayer.world, new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, "minecraft:enchanting_table", title));
        entityPlayer.activeContainer = fakeEnchant;
        entityPlayer.activeContainer.windowId = containerId;
        entityPlayer.activeContainer.addSlotListener(entityPlayer);

        InventoryOpenEvent inventoryEvent = new InventoryOpenEvent(fakeEnchant.getBukkitView());
        Bukkit.getServer().getPluginManager().callEvent(inventoryEvent);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player && event.getInventory().getType() == InventoryType.ENCHANTING) {
            event.getInventory().setItem(1, null);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getInventory().getType() == InventoryType.ENCHANTING) {
            if (event.getSlot() == 1 && event.getInventory().getItem(1) != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void openInventoryEvent(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player && event.getInventory().getType() == InventoryType.ENCHANTING) {
            Dye d = new Dye();
            d.setColor(DyeColor.BLUE);
            ItemStack lapis = d.toItemStack();
            lapis.setAmount(64);
            event.getInventory().setItem(1, lapis);
            ((Player) event.getPlayer()).updateInventory();
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
