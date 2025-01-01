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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Brand extends Kit {

    public Brand() {
        super("Brand",
                "legendhg.kits.brand",
                new ItemManager(Material.FLINT_AND_STEEL, Util.color3 + "Brand")
                        .setLore(Arrays.asList(Util.success + "Ganhe força", Util.success + "enquanto estiver pegando fogo."))
                        .build(),
                Arrays.asList(new ItemStack[]{new ItemManager(Material.FLINT_AND_STEEL, Util.color3 + "Brand").build()}),
                false);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player brand = (Player) event.getEntity();
        Account account = AccountManager.getOrCreateAccount(brand);
        if (!account.getKits().contains(this)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
            brand.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3 * 20, 0));
        }
    }

}
