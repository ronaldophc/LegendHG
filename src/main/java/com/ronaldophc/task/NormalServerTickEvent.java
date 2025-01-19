package com.ronaldophc.task;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NormalServerTickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public NormalServerTickEvent() {

    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
