package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;

public class Viking extends Kit {

    public Viking() {
        super("Viking",
                "legendhg.kits.viking",
                new ItemManager(Material.STONE_AXE, Util.color3 + "Viking")
                        .setLore(Arrays.asList(Util.success + "Batalhe como um", Util.success + "verdadeiro viking."))
                        .build(),
                null,
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
