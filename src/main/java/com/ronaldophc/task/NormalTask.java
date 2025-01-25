package com.ronaldophc.task;

import com.ronaldophc.LegendHG;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NormalTask {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void startTask() {
        scheduler.scheduleAtFixedRate(this::tick, 0, 1, TimeUnit.SECONDS);
    }

    private int secondsOnline = 0;

    public void tick() {
        secondsOnline += 1;
        new NormalServerTickEvent(secondsOnline).callEvent();
    }

    public void stopTask() {
        scheduler.shutdown();
    }
}
