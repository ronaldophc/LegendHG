package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Cultivator extends Kit {

    public Cultivator() {
        super("Cultivator",
                "legendhg.kits.cultivator",
                new ItemManager(Material.SAPLING, Util.color3 + "Cultivator")
                        .setLore(Arrays.asList(Util.success + "Ao plantar uma semente", Util.success + "nascerá instantaneamente"))
                        .build(),
                null,
                false);
    }

    @EventHandler
    public void onPlant(BlockPlaceEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;

        Player cultivator = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(cultivator);
        if (!account.getKits().contains(this)) return;

        Block eventBlock = event.getBlock();
        Material eventItem = event.getBlock().getType();
        if (eventItem == Material.SAPLING) {
            eventBlock.getWorld().generateTree(eventBlock.getLocation(), TreeType.TREE);
            eventBlock.setType(Material.LOG);
            return;
        }
        if (eventItem == Material.CROPS) {
            eventBlock.setData((byte) 7);
            return;
        }
        if (eventItem == Material.COCOA) {
            eventBlock.setData((byte) 8);
        }
    }
}
