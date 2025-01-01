package com.ronaldophc.task;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.ronaldophc.player.PlayerAliveManager;
import com.ronaldophc.player.PlayerSpectatorManager;

public class MainTask implements Runnable {

    private static final MainTask instance = new MainTask();

    @Override
    public void run() {
        World world = Bukkit.getWorld("world");
        world.setTime(1800);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (PlayerAliveManager.getInstance().isPlayerAlive(player.getUniqueId())) {
                continue;
            }
            if (PlayerSpectatorManager.getInstance().isPlayerSpectating(player)) {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (player2.isOp()) continue;
                    player2.hidePlayer(player);
                }
            }
        }

    }

    public static MainTask getInstance() {
        return instance;
    }
}
