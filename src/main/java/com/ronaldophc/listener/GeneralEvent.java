package com.ronaldophc.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEditBookEvent;

import java.util.Arrays;
import java.util.List;

public class GeneralEvent implements Listener {

    private static final List<String> blockedCommands = Arrays.asList(
            "/pl", "/plugins", "/?", "/rl", "/achievement", "/bukkit", "/ver", "/bukkit:me",
            "/help", "/me", "/reload", "/bukkit:plugins", "/stop", "/op", "/deop",
            "/wg", "/worldguard", "/we", "/worldedit", "/lp", "/luckperms", "/antiop",
            "/protocol", "/gpr", "/gprotector", "/god", "/ungod", "/fill", "/summon", "/setblock", "/execute", "/op"
    );

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0].toLowerCase();
        Player player = event.getPlayer();
        if (player.isOp()) return;

        if (blockedCommands.contains(command)) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onBookEdit(PlayerEditBookEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onRedstoneChange(BlockRedstoneEvent event) {
        if (event.getNewCurrent() > 10) {
            event.setNewCurrent(0); // Reseta a energia de redstone
        }
    }
}
