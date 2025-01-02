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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Berserker extends Kit {

    public Berserker() {
        super("Berserker",
                "legendhg.kits.berserker",
                new ItemManager(Material.SKULL_ITEM, Util.color3 + "Berserker")
                        .setData((byte) 3)
                        .setSkullOwner("Zombie")
                        .setLore(Arrays.asList(Util.success + "Ao matar um jogador", Util.success + "você ganhará força."))
                        .build(),
                null,
                false);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player killer = event.getEntity().getKiller();
        Account account = LegendHG.getAccountManager().getOrCreateAccount(killer);
        if (!account.getKits().contains(this)) return;
        killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1));
    }
}
