package com.ronaldophc.kits.registry.gladiator;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GladiatorEndsEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private boolean cancelled;

    public GladiatorEndsEvent(Player player) {
        this.player = player;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
