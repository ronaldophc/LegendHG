package com.ronaldophc.kits;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownAPI implements Runnable {

    @Getter
    private static final CooldownAPI instance = new CooldownAPI();

    private static Map<UUID, Map<Kit, Long>> cooldowns;
    private static Map<UUID, CombatLogInfo> combatLogCooldowns;

    private CooldownAPI() {
        cooldowns = new HashMap<>();
        combatLogCooldowns = new HashMap<>();
    }

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        for (Iterator<Map.Entry<UUID, Map<Kit, Long>>> it = cooldowns.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<UUID, Map<Kit, Long>> playerEntry = it.next();
            Map<Kit, Long> kitCooldowns = playerEntry.getValue();
            kitCooldowns.entrySet().removeIf(entry -> entry.getValue() < currentTime);
            if (kitCooldowns.isEmpty()) {
                it.remove();
            }
        }

        // Use an iterator to safely remove entries from combatLogCooldowns
        combatLogCooldowns.entrySet().removeIf(entry -> entry.getValue().getEndTime() < currentTime);
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

    public void setCombatLogCooldown(Player player, Player hitter, int seconds) {
        long endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
        combatLogCooldowns.put(player.getUniqueId(), new CombatLogInfo(hitter.getUniqueId(), endTime));
    }

    public boolean isOnCombatLogCooldown(Player player) {
        return combatLogCooldowns.containsKey(player.getUniqueId());
    }

    public int getCombatLogCooldown(Player player) {
        if (!isOnCombatLogCooldown(player)) return 0;
        return (int) ((combatLogCooldowns.get(player.getUniqueId()).getEndTime() - System.currentTimeMillis()) / 1000);
    }

    public UUID getCombatLogHitter(Player player) {
        if (!isOnCombatLogCooldown(player)) return null;
        return combatLogCooldowns.get(player.getUniqueId()).getHitterUUID();
    }

    public void removeCombatLogCooldown(Player player) {
        combatLogCooldowns.remove(player.getUniqueId());
    }

    @Getter
    private static class CombatLogInfo {
        private final UUID hitterUUID;
        private final long endTime;

        public CombatLogInfo(UUID hitterUUID, long endTime) {
            this.hitterUUID = hitterUUID;
            this.endTime = endTime;
        }

    }
}