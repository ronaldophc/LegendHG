package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class Worm extends Kit {

    public Worm() {
        super("Worm",
                "legendhg.kits.worm",
                new ItemManager(Material.DIRT, Util.color3 + "Worm")
                        .setLore(Arrays.asList(
                                Util.success + "Quebre terra instantaneamente.",
                                Util.success + "Ganhe regeneração ao quebra-las.",
                                Util.success + "Não tome dano de queda ao cair na terra."
                        ))
                        .build(),
                null,
                false);
    }

    @EventHandler
    public void onBreak(BlockDamageEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);

        if (!account.getKits().contains(this)) return;

        Block block = event.getBlock();
        if (block.getType() != Material.DIRT) return;

        block.breakNaturally();
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 5, 1));
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        Player player = (Player) event.getEntity();
        Location location = player.getLocation().clone().subtract(0, 1, 0);
        Block block = event.getWorld().getBlockAt(location);
        if (block.getType() == Material.DIRT) {
            event.setCancelled(true);
        }
    }
}
