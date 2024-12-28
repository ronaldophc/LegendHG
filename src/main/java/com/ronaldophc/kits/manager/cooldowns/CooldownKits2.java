package com.ronaldophc.kits.manager.cooldowns;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Util;
import com.ronaldophc.constant.Kits;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownKits2 implements Runnable {

    private static final CooldownKits2 instance = new CooldownKits2();

    private static HashMap<UUID, Long> cooldown;

    private CooldownKits2() {
        cooldown = new HashMap<>();
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<UUID, Long>> iterator = cooldown.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Long> entry = iterator.next();
            if (entry.getValue() < currentTime) {
                iterator.remove();
                if (isPlayerOnline(entry.getKey())) {
                    Player player = Bukkit.getPlayer(entry.getKey());
                    Kits kit = LegendHG.getKitManager().getPlayerKit2(player);
                    player.sendMessage(Util.success + "Você pode usar o Kit " + kit.getName() + " novamente.");
                    removeCooldown(player);
                }
            }
        }
    }

    public boolean isPlayerOnline(UUID uuid) {
        return Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).isOnline();
    }

    public void setCooldown(Player player, int seconds) {
        cooldown.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));
    }

    public boolean isOnCooldown(Player player) {
        return cooldown.containsKey(player.getUniqueId());
    }

    public int getCooldown(Player player) {
        return (int) ((cooldown.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000);
    }

    public void removeCooldown(Player player) {
        cooldown.remove(player.getUniqueId());
    }

    public static CooldownKits2 getInstance() {
        return instance;
    }
}