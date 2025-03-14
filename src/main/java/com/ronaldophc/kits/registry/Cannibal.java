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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class Cannibal extends Kit {

    public Cannibal() {
        super("Cannibal",
                "legendhg.kits.cannibal",
                new ItemManager(Material.ROTTEN_FLESH, Util.color3 + "Cannibal")
                        .setLore(Arrays.asList(Util.success + "Ao matar um jogador", Util.success + "você ganhará vida e regeneração."))
                        .build(),
                null,
                false);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player player = event.getEntity();

        if (event.getEntity().getKiller() == null) return;
        Player killer = player.getKiller();

        Account account = AccountManager.getInstance().getOrCreateAccount(killer);
        if (!account.getKits().contains(this)) return;

        killer.setHealth(Math.min(killer.getHealth() + 2, 20));
        killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 120, 1));
    }
}
