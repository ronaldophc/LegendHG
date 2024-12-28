package com.ronaldophc.kits.manager.kits;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GravityManager {

    private static GravityManager instance = new GravityManager();

    private GravityManager() {
    }

    public static GravityManager getInstance() {
        if (instance == null) {
            instance = new GravityManager();
        }
        return instance;
    }

    public List<UUID> gravityPlayers = new ArrayList<>();

    public void addPlayer(Player player) {
        gravityPlayers.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        gravityPlayers.remove(player.getUniqueId());
    }

    public boolean hasPlayer(Player player) {
        return gravityPlayers.contains(player.getUniqueId());
    }

    public List<UUID> getPlayers() {
        return gravityPlayers;
    }

}
