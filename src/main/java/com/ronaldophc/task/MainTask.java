package com.ronaldophc.task;

import com.ronaldophc.LegendHG;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class MainTask implements Runnable {

    @Getter
    private static final MainTask instance = new MainTask();

    @Override
    public void run() {
        World world = Bukkit.getWorld("world");
        world.setTime(1800);
        for (Player player : Bukkit.getOnlinePlayers()) {
            Account account = AccountManager.getInstance().getOrCreateAccount(player);
            if (account.isAlive()) {
                continue;
            }
            if (account.isSpectator()) {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (player2.isOp()) continue;
                    player2.hidePlayer(player);
                }
            }
        }

    }

}
