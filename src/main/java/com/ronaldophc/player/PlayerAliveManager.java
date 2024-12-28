package com.ronaldophc.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import com.ronaldophc.constant.GameState;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Logger;

public class PlayerAliveManager {

    public List<UUID> playersAlive = new ArrayList<>();
    private static PlayerAliveManager instance = new PlayerAliveManager();

    public static PlayerAliveManager getInstance() {
        if (instance == null) {
            instance = new PlayerAliveManager();
        }
        return instance;
    }

    private PlayerAliveManager() {
    }

    public void addPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        if (isPlayerAlive(player.getUniqueId())) {
            return;
        }
        playersAlive.add(uuid);
    }

    public void removePlayer(Player player) {
        LegendHG.logger.log(Level.INFO, "Removing player {0}", player.getName());
        playersAlive.remove(player.getUniqueId());

        if (LegendHG.getGameStateManager().getGameState() == GameState.COUNTDOWN) return;
        World world = Bukkit.getWorld("world");
        world.strikeLightningEffect(player.getLocation());
    }

    public List<UUID> getPlayersAlive() {
        return playersAlive;
    }

    public boolean isPlayerAlive(UUID player) {
        return playersAlive.contains(player);
    }

    public boolean isPlayerOnline(UUID uuid) {
        return Bukkit.getPlayer(uuid) != null;
    }

    public boolean hasMoreThanOnePlayerAlive() {
        if (playersAlive.isEmpty()) {
            Bukkit.shutdown();
            Logger.debug("hasMoreThanOnePlayerAlive: A lista de players estava vazia, por isso o servidor reiniciou!");
            return false;
        }
        return playersAlive.size() > 1;
    }

    public Player getWinner() {
        return Bukkit.getPlayer(playersAlive.get(0));
    }
}
