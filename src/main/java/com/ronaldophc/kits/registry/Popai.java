package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Popai extends Kit {

    public Popai() {
        super("Popai",
                "legendhg.kits.popai",
                new ItemManager(Material.CARROT_ITEM, Util.color3 + "Popai")
                        .setLore(Arrays.asList(Util.success + "Ao comer fique imune", Util.success + "a efeitos negativos", Util.success + "e ganhe regeneração por 60s."))
                        .build(),
                Arrays.asList(new ItemStack[]{new ItemManager(Material.CARROT_ITEM, Util.color3 + "Popai")
                        .setAmount(5)
                        .build()}),
                false);
    }

    @EventHandler
    public void onEat(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player popai = event.getPlayer();

        Account account = AccountManager.getOrCreateAccount(popai);
        if (!account.getKits().contains(this)) return;

        ItemStack itemInHand = popai.getItemInHand();
        if (event.getItem() == null) return;
        if (!isItemKit(event.getItem())) return;
        if ((event.getAction().name().contains("RIGHT"))) {
            popai.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60 * 20, 0));
            itemInHand.setAmount(itemInHand.getAmount() - 1);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player popai = (Player) event.getEntity();
        Account account = AccountManager.getOrCreateAccount(popai);
        if (!account.getKits().contains(this)) return;

        if ((event.getCause() == EntityDamageEvent.DamageCause.POISON
                || event.getCause() == EntityDamageEvent.DamageCause.WITHER
                || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                || event.getCause() == EntityDamageEvent.DamageCause.POISON) && (popai.hasPotionEffect(PotionEffectType.REGENERATION))) {
            event.setCancelled(true);
        }
    }
}