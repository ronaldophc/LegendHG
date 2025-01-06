package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Monk extends Kit {

    public Monk() {
        super("Monk",
                "legendhg.kits.monk",
                new ItemManager(Material.BLAZE_ROD, Util.color3 + "Monk")
                        .setLore(Arrays.asList(Util.success + "Use seu monk para", Util.success + "embaralhar o inventario", Util.success + "do seu inimigo!"))
                        .build(),
                new ItemManager(Material.BLAZE_ROD, Util.color3 + "Monk")
                        .setUnbreakable()
                        .build(),
                false);
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (!(entity instanceof Player)) return;
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) return;
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;
        if (!isItemKit(player.getItemInHand())) return;

        Player target = (Player) event.getRightClicked();

        if (kitManager.isOnCooldown(player, this)) return;
        kitManager.setCooldown(player, 15, this);

        int playerInvSize = player.getInventory().getSize();
        int randomSlot = new Random().nextInt(playerInvSize);

        ItemStack inHand = target.getItemInHand();
        ItemStack randomItem = target.getInventory().getItem(randomSlot);

        target.getInventory().setItem(randomSlot, inHand);
        target.getInventory().setItemInHand(randomItem);
    }
}
