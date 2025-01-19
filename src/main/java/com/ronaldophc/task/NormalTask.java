package com.ronaldophc.task;

import lombok.Getter;

public class NormalTask implements Runnable {

    @Getter
    private static final NormalTask instance = new NormalTask();

    @Override
    public void run() {
        new NormalServerTickEvent().callEvent();
    }

}
