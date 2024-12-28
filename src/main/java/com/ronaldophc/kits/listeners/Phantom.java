package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Phantom implements Listener {

    private final Kits PHANTOM = Kits.PHANTOM;
    private static final Map<UUID, ItemStack[]> armors = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        KitManager kitManager = LegendHG.getKitManager();
        if (kitManager.isThePlayerKit(player, Kits.PHANTOM) &&
                event.getCurrentItem() != null &&
                event.getCurrentItem().getType().toString().contains("LEATHER"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteractPhantom(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player phantom = event.getPlayer();
        KitManager kitManager = LegendHG.getKitManager();

        if (!kitManager.isThePlayerKit(phantom, PHANTOM)) return;
        if (event.getItem() == null) return;
        if (!kitManager.isItemKit(event.getItem(), PHANTOM)) return;
        if (kitManager.isOnCooldown(phantom, PHANTOM)) return;

        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestp = (LeatherArmorMeta) chest.getItemMeta();
        chestp.setColor(Color.WHITE);
        chest.setItemMeta((ItemMeta) chestp);

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(Color.WHITE);
        helmet.setItemMeta((ItemMeta) helmetMeta);

        ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta legsMeta = (LeatherArmorMeta) legs.getItemMeta();
        legsMeta.setColor(Color.WHITE);
        legs.setItemMeta((ItemMeta) legsMeta);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(Color.WHITE);
        boots.setItemMeta((ItemMeta) bootsMeta);

        kitManager.setCooldown(phantom, 40, PHANTOM);
        phantom.setAllowFlight(true);
        phantom.setFlying(true);
        armors.put(phantom.getUniqueId(), phantom.getInventory().getArmorContents());
        phantom.getInventory().setArmorContents(new ItemStack[4]);
        phantom.getInventory().setChestplate(chest);
        phantom.getInventory().setHelmet(helmet);
        phantom.getInventory().setLeggings(legs);
        phantom.getInventory().setBoots(boots);
        phantom.updateInventory();
        (new BukkitRunnable() {
            public void run() {
                phantom.sendMessage(Util.success + "Você pode voar por mais 5 segundos.");
            }
        }).runTaskLater(LegendHG.getInstance(), 40L);
        (new BukkitRunnable() {
            public void run() {
                phantom.sendMessage(Util.success + "Você pode voar por mais 3 segundos.");
            }
        }).runTaskLater(LegendHG.getInstance(), 60L);
        (new BukkitRunnable() {
            public void run() {
                phantom.sendMessage(Util.success + "Você pode voar por mais 1 segundo.");
            }
        }).runTaskLater(LegendHG.getInstance(), 80L);
        (new BukkitRunnable() {
            public void run() {
                phantom.setFlying(false);
                phantom.setAllowFlight(false);
                phantom.updateInventory();
                phantom.getInventory().setArmorContents(armors.get(phantom.getUniqueId()));
                armors.remove(phantom.getUniqueId());
            }
        }).runTaskLater(LegendHG.getInstance(), 100L);
    }

}