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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class Camel extends Kit {

    public Camel() {
        super("Camel",
                "legendhg.kits.camel",
                new ItemManager(Material.SAND, Util.color3 + "Camel")
                        .setLore(Arrays.asList(Util.success + "Ao andar por uma areia", Util.success + "você ganhará velocidade."))
                        .build(),
                null,
                false);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;
        if (player.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.SAND) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 8, 1));
        }
    }
}
