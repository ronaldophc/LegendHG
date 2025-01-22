package com.ronaldophc.feature.punish;

import com.ronaldophc.LegendHG;
import com.ronaldophc.database.BanIPRepository;
import com.ronaldophc.database.BanRepository;
import com.ronaldophc.database.MuteRepository;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PunishManager {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void startUnbanTask() {
        scheduler.scheduleAtFixedRate(this::undoExpiredPunishments, 0, 3, TimeUnit.MINUTES);
    }

    private void undoExpiredPunishments() {
        if (LegendHG.getInstance().devMode) {
            LegendHG.logger.info("Setando active como false das punições expiradas.");
        }
        MuteRepository.unMuteExpired();
        BanIPRepository.unbanExpiredIPs();
        BanRepository.unbanExpired();
    }

    public void stopPunishTask() {
        scheduler.shutdown();
    }
}
