package com.ronaldophc.kits.manager.kits;

import com.ronaldophc.LegendHG;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class NinjaManager {

    private static NinjaManager instance = new NinjaManager();

    public static HashMap<UUID, UUID> ninjaPull;
    public static HashMap<UUID, Integer> ninjaPullTimer;

    private NinjaManager() {
        ninjaPull = new HashMap<>();
        ninjaPullTimer = new HashMap<>();
    }

    public static NinjaManager getInstance() {
        if (instance == null) {
            instance = new NinjaManager();
        }
        return instance;
    }

    public void pull(Player ninja, Player target) {
        ninjaPull.put(ninja.getUniqueId(), target.getUniqueId());
        resetTargetTimer(ninja);
    }

    public boolean isTargetOnline(Player player) {
        UUID targetUUID = ninjaPull.get(player.getUniqueId());
        if (targetUUID == null) {
            return false;
        }
        Player targetPlayer = Bukkit.getPlayer(targetUUID);
        return targetPlayer != null && targetPlayer.isOnline();
    }

    public boolean hasTarget(Player player) {
        return ninjaPull.containsKey(player.getUniqueId());
    }

    public Player getTarget(Player player) {
        UUID targetUUID = ninjaPull.get(player.getUniqueId());
        return Bukkit.getPlayer(targetUUID);
    }

    public void removeTarget(Player player) {
        ninjaPull.remove(player.getUniqueId());
        cancelTargetTimer(player);
    }

    public void resetTargetTimer(Player ninja) {
        cancelTargetTimer(ninja);
        int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(LegendHG.getInstance(), () -> removeTarget(ninja), 160L); // 8 segundos
        ninjaPullTimer.put(ninja.getUniqueId(), taskId);
    }

    private void cancelTargetTimer(Player ninja) {
        if (ninjaPullTimer.containsKey(ninja.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(ninjaPullTimer.get(ninja.getUniqueId()));
            ninjaPullTimer.remove(ninja.getUniqueId());
        }
    }

}
