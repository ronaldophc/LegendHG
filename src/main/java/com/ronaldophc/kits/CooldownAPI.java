package com.ronaldophc.kits;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownAPI implements Runnable {

    private static final CooldownAPI instance = new CooldownAPI();

    private static Map<UUID, Map<Kit, Long>> cooldowns;

    private CooldownAPI() {
        cooldowns = new HashMap<>();
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<UUID, Map<Kit, Long>> playerEntry : cooldowns.entrySet()) {
            UUID playerUUID = playerEntry.getKey();
            Map<Kit, Long> kitCooldowns = playerEntry.getValue();
            kitCooldowns.entrySet().removeIf(entry -> entry.getValue() < currentTime);
            if (kitCooldowns.isEmpty()) {
                cooldowns.remove(playerUUID);
            }
        }
    }

    public void setCooldown(Player player, Kit kit, int seconds) {
        long time = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(kit, time);
    }

    public boolean isOnCooldown(Player player, Kit kit) {
        return cooldowns.containsKey(player.getUniqueId()) && cooldowns.get(player.getUniqueId()).containsKey(kit);
    }

    public int getCooldown(Player player, Kit kit) {
        if (!isOnCooldown(player, kit)) return 0;
        return (int) ((cooldowns.get(player.getUniqueId()).get(kit) - System.currentTimeMillis()) / 1000);
    }

    public void removeCooldown(Player player, Kit kit) {
        if (cooldowns.containsKey(player.getUniqueId())) {
            cooldowns.get(player.getUniqueId()).remove(kit);
            if (cooldowns.get(player.getUniqueId()).isEmpty()) {
                cooldowns.remove(player.getUniqueId());
            }
        }
    }

    public static CooldownAPI getInstance() {
        return instance;
    }
}