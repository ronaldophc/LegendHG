package com.ronaldophc.task;


import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class NormalServerTickEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final int secondsOnline;

    public NormalServerTickEvent(int secondsOnline) {
        this.secondsOnline = secondsOnline;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
