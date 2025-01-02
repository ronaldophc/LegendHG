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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Poseidon extends Kit {

    public int potionMultiplier = 0;

    public Poseidon() {
        super("Poseidon",
                "legendhg.kits.poseidon",
                new ItemManager(Material.WATER_BUCKET, Util.color3 + "Poseidon")
                        .setLore(Arrays.asList(Util.success + "Fique forte", Util.success + "ao entrar na agua!"))
                        .build(),
                null,
                false);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player player = event.getPlayer();
        Account account = LegendHG.getAccountManager().getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;
        if (player.getRemainingAir() < 200) {
            player.setRemainingAir(200);
        }
        if (player.getLocation().getBlock().isLiquid()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40, this.potionMultiplier), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, this.potionMultiplier), true);
        }
    }

}
