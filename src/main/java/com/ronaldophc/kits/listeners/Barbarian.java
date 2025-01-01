package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.database.CurrentGameSQL;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Barbarian implements Listener {

    public enum BarbarianSwords {
        FIRST_SWORD(new ItemManager(Material.WOOD_SWORD, Util.color3 + "Barbarian").setUnbreakable().build()),
        SECOND_SWORD(new ItemManager(Material.STONE_SWORD, Util.color3 + "Barbarian").setUnbreakable().build()),
        THIRD_SWORD(new ItemManager(Material.IRON_SWORD, Util.color3 + "Barbarian").setUnbreakable().build()),
        FOURTH_SWORD(new ItemManager(Material.DIAMOND_SWORD, Util.color3 + "Barbarian").setUnbreakable().build()),
        FIFTH_SWORD(new ItemManager(Material.DIAMOND_SWORD, Util.color3 + "Barbarian").setUnbreakable().addEnchantment(Enchantment.DAMAGE_ALL, 1).build()),
        SIXTH_SWORD(new ItemManager(Material.DIAMOND_SWORD, Util.color3 + "Barbarian").setUnbreakable().addEnchantment(Enchantment.DAMAGE_ALL, 2).build());

        private final ItemStack item;

        BarbarianSwords(ItemStack swordStack) {
            this.item = swordStack;
        }

        public ItemStack getItem() {
            return item;
        }
    }

    Kits BARBARIAN = Kits.BARBARIAN;

    @EventHandler(priority = EventPriority.HIGH)
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;

        Player killer = event.getEntity().getKiller();
        if (!LegendHG.getKitManager().isThePlayerKit(killer, BARBARIAN)) {
            return;
        }

        try {
            int kills = CurrentGameSQL.getCurrentGameKills(killer, LegendHG.getGameId());
            switch (kills) {
                case 1:
                    killer.getInventory().remove(BarbarianSwords.FIRST_SWORD.getItem());
                    killer.getInventory().addItem(BarbarianSwords.SECOND_SWORD.getItem());
                    break;
                case 4:
                    killer.getInventory().remove(BarbarianSwords.SECOND_SWORD.getItem());
                    killer.getInventory().addItem(BarbarianSwords.THIRD_SWORD.getItem());
                    break;
                case 8:
                    killer.getInventory().remove(BarbarianSwords.THIRD_SWORD.getItem());
                    killer.getInventory().addItem(BarbarianSwords.FOURTH_SWORD.getItem());
                    break;
                case 12:
                    killer.getInventory().remove(BarbarianSwords.FOURTH_SWORD.getItem());
                    killer.getInventory().addItem(BarbarianSwords.FIFTH_SWORD.getItem());
                    break;
                case 15:
                    killer.getInventory().remove(BarbarianSwords.FIFTH_SWORD.getItem());
                    killer.getInventory().addItem(BarbarianSwords.SIXTH_SWORD.getItem());
            }
        } catch (Exception e) {
            Logger.logError("Barbarian onKill: " + e.getMessage());
        }
    }
}
