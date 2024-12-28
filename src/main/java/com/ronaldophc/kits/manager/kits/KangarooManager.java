package com.ronaldophc.kits.manager.kits;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class KangarooManager {

    private static KangarooManager instance = new KangarooManager();

    private KangarooManager() {
        kangarooPlayers = new HashMap<>();
    }

    public static KangarooManager getInstance() {
        if (instance == null) {
            instance = new KangarooManager();
        }
        return instance;
    }

    public HashMap<UUID, Integer> kangarooPlayers;

    public void setPlayer(Player player, int times) {
        kangarooPlayers.put(player.getUniqueId(), times);
    }

    public void removePlayer(Player player) {
        kangarooPlayers.remove(player.getUniqueId());
    }

    public int getPlayer(Player player) {
        return kangarooPlayers.get(player.getUniqueId());
    }

    public boolean hasPlayer(Player player) {
        return kangarooPlayers.containsKey(player.getUniqueId());
    }

}
