package com.ronaldophc.kits.registry;

import com.ronaldophc.feature.CustomEnchant;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import java.util.Arrays;

public class Specialist extends Kit {

    public Specialist() {
        super("Specialist",
                "legendhg.kits.specialist",
                new ItemManager(Material.BOOK, Util.color3 + "Specialist")
                        .setLore(Arrays.asList(Util.success + "Crie itens encantados", Util.success + "com um livro."))
                        .build(),
                new ItemManager(Material.BOOK, Util.color3 + "Specialist")
                        .build(),
                false);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;

        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;

        if (!isItemKit(player.getItemInHand())) return;

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        int containerId = entityPlayer.nextContainerCounter();
        IChatBaseComponent title = new ChatMessage("Specialist", new Object[]{});

        CustomEnchant fakeEnchant = new CustomEnchant(entityPlayer.inventory, entityPlayer.world, new BlockPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ()));
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
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;
        player.setLevel(player.getLevel() + 1);
    }


    @EventHandler
    public void onPrepare(PrepareItemEnchantEvent event) {
        event.getExpLevelCostsOffered()[0] = 1;
        event.getExpLevelCostsOffered()[1] = 1;
        event.getExpLevelCostsOffered()[2] = 1;
    }

}
