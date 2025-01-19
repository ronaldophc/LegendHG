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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class Scout extends Kit {

    public Scout() {
        super("Scout",
                "legendhg.kits.scout",
                new ItemManager(Material.POTION, Util.color3 + "Scout")
                        .setLore(Arrays.asList(Util.success + "Inicie a partida com", Util.success + "3 poções de força!"))
                        .setDurability(8194)
                        .build(),
                new ItemManager(Material.POTION, Util.color3 + "Scout")
                        .setDurability(8194)
                        .build(),
                false);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;
        if (!isItemKit(event.getItem())) return;
        event.setCancelled(true);
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) {
            return;
        }
        if (!event.getAction().name().contains("RIGHT")) return;
        if (kitManager.isOnCooldown(player, this)) return;
        kitManager.setCooldown(player, 30, this);
        player.addPotionEffect(PotionEffectType.SPEED.createEffect(20 * 14, 1));
    }
}
