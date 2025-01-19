package com.ronaldophc.api.cooldown;

import com.ronaldophc.LegendHG;
import com.ronaldophc.api.actionbar.ActionBarAPI;
import com.ronaldophc.constant.CooldownType;
import com.ronaldophc.constant.MCVersion;
import com.ronaldophc.util.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.task.NormalServerTickEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownAPI implements Listener {

    private static CooldownAPI instance;

    private static Map<UUID, Map<Kit, Long>> cooldowns;
    private static Map<UUID, CombatLogInfo> combatLogCooldowns;

    private CooldownAPI() {
        cooldowns = new HashMap<>();
        combatLogCooldowns = new HashMap<>();
    }

    @EventHandler
    public void onServerTick(NormalServerTickEvent event) {
        tick();
    }

    public void tick() {
        long currentTime = System.currentTimeMillis();
        for (Iterator<Map.Entry<UUID, Map<Kit, Long>>> it = cooldowns.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<UUID, Map<Kit, Long>> playerEntry = it.next();
            Map<Kit, Long> kitCooldowns = playerEntry.getValue();
            kitCooldowns.entrySet().removeIf(entry -> entry.getValue() < currentTime);
            if (kitCooldowns.isEmpty()) {
                it.remove();
            }
        }

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

    public static void sendActionBar(Player player, String message) {
        ActionBarAPI.send(player, message);
    }

    public static void sendCooldownBar(Player player, Kit kit) {
        Account account = AccountManager.getInstance().getOrCreateAccount(player);

        KitManager kitManager = LegendHG.getKitManager();
        String message = Util.bold + Util.color1 + kit.getName() + ": " + Util.error + Util.bold + (kitManager.getCooldown(player, kit) + 1) + "s" + Util.bold + Util.color2 + " para usar novamente.";

        if (account.getVersion() == MCVersion.MC_1_7 || account.getCooldownType() == CooldownType.CHAT) {
            player.sendMessage(message);
            return;
        }
        sendActionBar(player, message);
    }

    public static void sendCombatLogCooldownBar(Player player) {
        Account account = AccountManager.getInstance().getOrCreateAccount(player);

        KitManager kitManager = LegendHG.getKitManager();
        String message = Util.error + Util.bold + (kitManager.getCombatLogCooldown(player) + 1) + "s" + Util.bold + Util.color2 + " para sair do combate.";

        if (account.getVersion() == MCVersion.MC_1_7 || account.getCooldownType() == CooldownType.CHAT) {
            player.sendMessage(message);
            return;
        }
        sendActionBar(player, message);
    }

    public static synchronized CooldownAPI getInstance() {
        if (instance == null) {
            instance = new CooldownAPI();
        }
        return instance;
    }
}