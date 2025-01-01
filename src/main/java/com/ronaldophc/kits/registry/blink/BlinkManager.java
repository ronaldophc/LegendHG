package com.ronaldophc.kits.registry.blink;


import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BlinkManager {

    public HashMap<UUID, Integer> blinkPlayers;
    private static BlinkManager instance = new BlinkManager();

    private BlinkManager() {
        blinkPlayers = new HashMap<>();
    }

    public void setBlinkPlayer(Player player, int times) {
        blinkPlayers.put(player.getUniqueId(), times);
    }

    public boolean isSetBlinkPlayer(Player player) {
        return blinkPlayers.containsKey(player.getUniqueId());
    }

    public int getTimes(Player player) {
        return blinkPlayers.get(player.getUniqueId());
    }

    public static BlinkManager getInstance() {
        if (instance == null) {
            instance = new BlinkManager();
        }
        return instance;
    }
}
