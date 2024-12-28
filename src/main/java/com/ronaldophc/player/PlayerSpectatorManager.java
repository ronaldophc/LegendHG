package com.ronaldophc.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerSpectatorManager {

    public List<UUID> playersSpectate = new ArrayList<>();
    private static PlayerSpectatorManager instance = new PlayerSpectatorManager();

    public static PlayerSpectatorManager getInstance() {
        if (instance == null) {
            instance = new PlayerSpectatorManager();
        }
        return instance;
    }

    private PlayerSpectatorManager() {
    }

    public void addPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        if (isPlayerSpectating(player)) {
            removePlayer(player);
        }
        playersSpectate.add(uuid);
    }

    public void removePlayer(Player player) {
        playersSpectate.remove(player.getUniqueId());
    }

    public void removePlayer(UUID uuid) {
        playersSpectate.remove(uuid);
    }

    public List<UUID> getPlayersSpectate() {
        return playersSpectate;
    }

    public boolean isPlayerSpectating(Player player) {
        return playersSpectate.contains(player.getUniqueId());
    }

    public boolean isPlayerOnline(UUID uuid) {
        return Bukkit.getPlayer(uuid) != null;
    }

}
