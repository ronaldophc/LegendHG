package com.ronaldophc.feature.punish.banip;

import com.ronaldophc.LegendHG;
import com.ronaldophc.database.BanRepository;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BanIPManager {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void startUnbanTask() {
        scheduler.scheduleAtFixedRate(this::unbanExpiredIPs, 0, 3, TimeUnit.MINUTES);
    }

    private void unbanExpiredIPs() {
        if (LegendHG.getInstance().devMode) {
            LegendHG.logger.info("Desbanindo IPS expirados.");
        }
        BanRepository.unbanExpiredIPs();
    }

    public void stopUnbanTask() {
        scheduler.shutdown();
    }
}
