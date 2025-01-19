package com.ronaldophc.kits.registry.gladiator;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class GladiatorEndsEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    @Setter
    private boolean cancelled;

    public GladiatorEndsEvent(Player player) {
        this.player = player;
        this.cancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
