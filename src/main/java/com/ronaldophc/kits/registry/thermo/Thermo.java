package com.ronaldophc.kits.registry.thermo;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class Thermo extends Kit {

    private final BlockTransformer blockTransformer;

    public Thermo() {
        super("Thermo",
                "legendhg.kits.thermo",
                new ItemManager(Material.DAYLIGHT_DETECTOR, Util.color3 + "Thermo")
                        .setLore(Collections.singletonList(Util.success + "Inverta Agua e Lava"))
                        .build(),
                new ItemManager(Material.DAYLIGHT_DETECTOR, Util.color3 + "Thermo")
                        .setUnbreakable()
                        .build(),
                false);
        this.blockTransformer = new BlockTransformer();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!account.getKits().contains(this)) return;

        ItemStack itemInHand = player.getItemInHand();
        if (!isItemKit(itemInHand)) return;
        if (kitManager.isOnCooldown(player, this)) return;

        Block blockClicked = event.getClickedBlock();
        if (blockClicked == null) {
            return;
        }

        blockTransformer.transformBlocks(player, blockClicked);
    }

}