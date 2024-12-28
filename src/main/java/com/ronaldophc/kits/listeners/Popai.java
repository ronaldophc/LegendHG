package com.ronaldophc.kits.listeners;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Popai implements Listener {

    @EventHandler
    public void onEat(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player popai = event.getPlayer();
        KitManager kitManager = LegendHG.getKitManager();

        if (!kitManager.isThePlayerKit(popai, Kits.POPAI)) return;

        ItemStack itemInHand = popai.getItemInHand();
        if (event.getItem() == null) return;
        if (!kitManager.isItemKit(event.getItem(), Kits.POPAI)) return;
        if ((event.getAction().name().contains("RIGHT"))) {
            popai.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60 * 20, 0));
            itemInHand.setAmount(itemInHand.getAmount() - 1);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player popai = (Player) event.getEntity();
        if (!LegendHG.getKitManager().isThePlayerKit(popai, Kits.POPAI)) return;

        if ((event.getCause() == EntityDamageEvent.DamageCause.POISON
                || event.getCause() == EntityDamageEvent.DamageCause.WITHER
                || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                || event.getCause() == EntityDamageEvent.DamageCause.POISON) && (popai.hasPotionEffect(PotionEffectType.REGENERATION))) {
            event.setCancelled(true);
        }
    }
}
