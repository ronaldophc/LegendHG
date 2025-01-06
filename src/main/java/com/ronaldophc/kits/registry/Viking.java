package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Viking extends Kit {

    public Viking() {
        super("Viking",
                "legendhg.kits.viking",
                new ItemManager(Material.STONE_AXE, Util.color3 + "Viking")
                        .setLore(Arrays.asList(Util.success + "Batalhe como um", Util.success + "verdadeiro viking."))
                        .build(),
                new ItemManager(Material.STONE_AXE, Util.color3 + "Viking")
                        .setUnbreakable()
                        .build(),
                false);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player damager = (Player) event.getDamager();

        Account account = AccountManager.getInstance().getOrCreateAccount(damager);
        if (!account.getKits().contains(this)) return;

        if (damager.getInventory().getItemInHand().getType().name().contains("AXE")) {
            event.setDamage(event.getFinalDamage() + 2);
        }
    }

}
