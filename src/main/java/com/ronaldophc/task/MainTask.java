package com.ronaldophc.task;

import com.ronaldophc.api.bossbar.BossBarAPI;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MainTask implements Runnable {

    @Getter
    private static final MainTask instance = new MainTask();
    private final ChatColor[] colors = Arrays.copyOfRange(ChatColor.values(), 0, 9);
    private int colorIndex = 0;

    @Override
    public void run() {
        World world = Bukkit.getWorld("world");
        world.setTime(1800);
        for (Player player : Bukkit.getOnlinePlayers()) {
            Account account = AccountManager.getInstance().getOrCreateAccount(player);
            if(account.getVersion() == null) continue;

            if (!BossBarAPI.hasBar(player)) {
                BossBarAPI.setBar(player, Util.bold + colors[colorIndex] + "LegendHG", 1);
                colorIndex = (colorIndex + 1) % colors.length;
            }

            if (account.isAlive()) {
                continue;
            }

            if (account.isSpectator()) {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    if (player2.isOp()) continue;
                    if (!(player2.canSee(player))) continue;
                    player2.hidePlayer(player);
                }
            }
        }

    }

}
