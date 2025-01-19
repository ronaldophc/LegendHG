package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Logger;
import com.ronaldophc.util.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Barbarian extends Kit {

    public Barbarian() {
        super("Barbarian",
                "legendhg.kits.barbarian",
                new ItemManager(Material.DIAMOND_SWORD, Util.color3 + "Barbarian").setLore(Arrays.asList(Util.success + "Ao matar um jogador", Util.success + "evolua sua espada.")).addEnchantment(Enchantment.DAMAGE_ALL, 2).build(),
                null,
                false);
    }

    @Override
    public void apply(Player player) {
        player.getInventory().addItem(BarbarianSwords.FIRST_SWORD.getItem());
    }

    @Getter
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

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getName().equalsIgnoreCase("Barbarian")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player killer = event.getEntity().getKiller();
        Account account = AccountManager.getInstance().getOrCreateAccount(killer);
        if (!account.getKits().contains(this)) return;

        try {
            int kills = account.getKills();
            switch (kills) {
                case 1:
                    killer.getInventory().remove(BarbarianSwords.FIRST_SWORD.getItem());
                    killer.getInventory().addItem(BarbarianSwords.SECOND_SWORD.getItem());
                    break;
                case 3:
                    killer.getInventory().remove(BarbarianSwords.SECOND_SWORD.getItem());
                    killer.getInventory().addItem(BarbarianSwords.THIRD_SWORD.getItem());
                    break;
                case 7:
                    killer.getInventory().remove(BarbarianSwords.THIRD_SWORD.getItem());
                    killer.getInventory().addItem(BarbarianSwords.FOURTH_SWORD.getItem());
                    break;
                case 11:
                    killer.getInventory().remove(BarbarianSwords.FOURTH_SWORD.getItem());
                    killer.getInventory().addItem(BarbarianSwords.FIFTH_SWORD.getItem());
                    break;
                case 14:
                    killer.getInventory().remove(BarbarianSwords.FIFTH_SWORD.getItem());
                    killer.getInventory().addItem(BarbarianSwords.SIXTH_SWORD.getItem());
            }
        } catch (Exception e) {
            Logger.logError("Barbarian onKill: " + e.getMessage());
        }
    }
}
