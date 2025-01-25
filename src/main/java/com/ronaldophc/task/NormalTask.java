package com.ronaldophc.task;

import lombok.Getter;

public class NormalTask implements Runnable {

    @Getter
    private static final NormalTask instance = new NormalTask();
    private int secondsOnline = 0;

    @Override
    public void run() {
        secondsOnline += 1;
        new NormalServerTickEvent(secondsOnline).callEvent();
    }
}
