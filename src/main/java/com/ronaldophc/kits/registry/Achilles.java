package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;

public class Achilles extends Kit {

    public Achilles() {
        super("Achilles",
                "legendhg.kits.achilles", new ItemManager(Material.WOOD_SWORD, Util.color3 + "Achilles").setLore(Arrays.asList(Util.success + "Tome mais dano para", Util.success + "espada de madeira", Util.success + "e menos das outras espadas.")).build(),
                null,
                false);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
            Player damager = (Player) event.getDamager();
            Player player = (Player) event.getEntity();
            Account account = AccountManager.getInstance().getOrCreateAccount(player);
            if (!account.getKits().contains(this)) return;
            if (damager.getItemInHand().getType().name().contains("WOOD_")) {
                event.setDamage(event.getDamage() * 1.5);
                return;
            }
            event.setDamage(event.getDamage() * 0.5);
        }
    }

}
