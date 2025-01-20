package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.feature.punish.banip.BanIP;
import com.ronaldophc.feature.punish.banip.BanIPService;
import com.ronaldophc.setting.Settings;
import com.ronaldophc.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class PlayerPreLogin implements Listener {

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        BanIPService banIPService = new BanIPService();
        InetAddress address = event.getAddress();
        if (banIPService.isIPBanned(address)) {
            BanIP banIP = banIPService.getActiveBanIP(address);

            String banMessage = getString(banIP);

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, banMessage);
        }
        for (Player player : LegendHG.getInstance().getServer().getOnlinePlayers()) {
            if (player.getAddress().getAddress().equals(event.getAddress())) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Util.title + "\n\n" + Util.errorServer + Util.bold + "Você já está conectado com este IP.");
            }
        }
    }

    private static String getString(BanIP banIP) {
        String expireAt = "§aIP banido até: " + Util.color3 + banIP.getExpire_atFormated();
        String reason = "§aMotivo: " + Util.color3 + banIP.getReason();
        String bannedAt = "§aData do banimento: " + Util.color3 + banIP.getBanned_atFormated();
        String linkDiscord = Settings.getInstance().getString("Discord");

        return Util.title + "\n\n"
                + expireAt + "\n"
                + reason + "\n"
                + bannedAt + "\n"
                + "§aDiscord: " + Util.color3 + linkDiscord;
    }
}
