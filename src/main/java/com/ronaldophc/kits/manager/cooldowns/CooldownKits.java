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

public class CooldownKits implements Runnable {

    private static final CooldownKits instance = new CooldownKits();

    private static HashMap<UUID, Long> cooldown;

    private CooldownKits() {
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
                    Kits kit = LegendHG.getKitManager().getPlayerKit(player);
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
        long time = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
        cooldown.put(player.getUniqueId(), time);
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

    public static CooldownKits getInstance() {
        return instance;
    }
}