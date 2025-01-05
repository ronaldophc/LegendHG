package com.ronaldophc.kits.registry.gladiator;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Gladiator extends Kit {

    public Gladiator() {
        super("Gladiator",
                "legendhg.kits.gladiator",
                new ItemManager(Material.IRON_FENCE, Util.color3 + "Gladiator")
                        .setLore(Arrays.asList(Util.success + "Desafie jogadores para 1x1", Util.success + "em uma arena no céu."))
                        .build(),
                new ItemManager(Material.IRON_FENCE, Util.color3 + "Gladiator")
                        .setUnbreakable()
                        .build(),
                false);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player gladiator = event.getPlayer();

        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        if (LegendHG.getGameStateManager().getGameState() == GameState.INVINCIBILITY) return;

        Account account = LegendHG.getAccountManager().getOrCreateAccount(gladiator);
        if (!account.getKits().contains(this)) return;

        if (!(event.getRightClicked() instanceof Player)) return;

        Player target = (Player) event.getRightClicked();
        Account targetAccount = LegendHG.getAccountManager().getOrCreateAccount(target);
        if (!targetAccount.isAlive()) return;
        if (!isItemKit(gladiator.getItemInHand())) return;

        event.setCancelled(true);

        if (kitManager.isOnCooldown(gladiator, this)) return;
        GladiatorController gladiatorController = LegendHG.getGladiatorController();

        gladiatorController.startGladiatorFight(gladiator, target);
    }

    @EventHandler
    public void onGladiatorEnds(GladiatorEndsEvent event) {
        LegendHG.getKitManager().setCooldown(event.getPlayer(), 5, this);
    }

}