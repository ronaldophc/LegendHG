package com.ronaldophc.task;

import com.ronaldophc.api.bossbar.BossBarAPI;
import com.ronaldophc.util.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FastTask implements Runnable {

    @Getter
    private static final FastTask instance = new FastTask();
    private final ChatColor[] colors = Arrays.copyOfRange(ChatColor.values(), 0, 9);
    private int colorIndex = 0;

    @Override
    public void run() {
        World world = Bukkit.getWorld("world");
        world.setTime(1800);
        for (Player player : Bukkit.getOnlinePlayers()) {
            Account account = AccountManager.getInstance().getOrCreateAccount(player);
            if (account.getVersion() == null) continue;

            if (!BossBarAPI.hasBar(player)) {
                BossBarAPI.setBar(player, Util.bold + colors[colorIndex] + "LegendHG", 1);
                colorIndex = (colorIndex + 1) % colors.length;
            }

            for (Player player2 : Bukkit.getOnlinePlayers()) {
                Account account2 = AccountManager.getInstance().getOrCreateAccount(player2);
                boolean canSeePlayer = player2.canSee(player);
                boolean isOpOrSeeSpecs = player2.isOp() && account2.isSeeSpecs() || account2.isSeeSpecs();

                if (isOpOrSeeSpecs || (!account.isSpectator() && !account.isVanish())) {
                    if (!canSeePlayer) player2.showPlayer(player);
                    continue;
                }
                if (account.isSpectator() && canSeePlayer || account.isVanish() && canSeePlayer) {
                    player2.hidePlayer(player);
                }
            }
        }
    }

}
