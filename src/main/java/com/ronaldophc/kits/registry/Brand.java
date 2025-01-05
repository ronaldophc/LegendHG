package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class Brand extends Kit {

    public Brand() {
        super("Brand",
                "legendhg.kits.brand",
                new ItemManager(Material.FLINT_AND_STEEL, Util.color3 + "Brand")
                        .setLore(Arrays.asList(Util.success + "Ganhe força", Util.success + "enquanto estiver pegando fogo."))
                        .build(),
                new ItemManager(Material.FLINT_AND_STEEL, Util.color3 + "Brand")
                        .setUnbreakable()
                        .build(),
                false);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player brand = (Player) event.getEntity();
        Account account = LegendHG.getAccountManager().getOrCreateAccount(brand);
        if (!account.getKits().contains(this)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
            brand.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20, 0));
        }
    }

}
