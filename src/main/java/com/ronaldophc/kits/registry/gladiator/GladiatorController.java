package com.ronaldophc.kits.registry.gladiator;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class GladiatorController {

    private final Set<GladiatorFight> activeFights = new HashSet<>();

    public GladiatorController() {
        GladiatorController instance = this;
    }

    public void startGladiatorFight(Player gladiator, Player target) {
        Location location = gladiator.getLocation();
        location.setY(110);

        while (isLocationOccupied(location)) {
            location.add(17, 0, 0);
        }

        if (isPlayerInFight(gladiator) || isPlayerInFight(target)) return;

        GladiatorFight fight = new GladiatorFight(gladiator, target, location, gladiator.getLocation());
        activeFights.add(fight);
    }

    public boolean isBlockOfArena(Location location) {
        for (GladiatorFight fight : activeFights) {
            if (fight.arenaBlocks.contains(location.getBlock()) || fight.placedBlocks.contains(location.getBlock())) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlayerInFight(Player player) {
        for (GladiatorFight fight : activeFights) {
            if (fight.isInFight(player)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLocationOccupied(Location location) {
        for (GladiatorFight fight : activeFights) {
            if (fight.hasArenaAbove(location)) {
                return true;
            }
        }
        return false;
    }

    public void endGladiatorFight(GladiatorFight fight) {
        activeFights.remove(fight);
    }

}
