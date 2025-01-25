package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.feature.punish.ban.Ban;
import com.ronaldophc.feature.punish.ban.BanService;
import com.ronaldophc.feature.punish.banip.BanIP;
import com.ronaldophc.feature.punish.banip.BanIPService;
import com.ronaldophc.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

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

        BanService banService = new BanService();
        if (banService.isBanned(event.getUniqueId())) {
            Ban ban = banService.getActiveBan(event.getUniqueId());

            String banMessage = getMessage(ban);

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, banMessage);
        }

        if (LegendHG.getInstance().devMode) {
            return;
        }

        for (Player player : LegendHG.getInstance().getServer().getOnlinePlayers()) {
            if (player.getAddress().getAddress().equals(event.getAddress())) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Util.title + "\n\n" + Util.errorServer + Util.bold + "Você já está conectado com este IP.");
            }
        }
    }

    public static String linkDiscord = LegendHG.settings.getString("Discord");

    private static String getMessage(Ban ban) {
        String expireAt = "§aBanido até: " + Util.color3 + ban.getExpire_atFormated();
        String reason = "§aMotivo: " + Util.color3 + ban.getReason();
        String bannedAt = "§aData do banimento: " + Util.color3 + ban.getBanned_atFormated();

        return Util.title + "\n\n"
                + "§a§lVocê foi banido!\n"
                + expireAt + "\n"
                + reason + "\n"
                + bannedAt + "\n"
                + "§aDiscord: " + Util.color3 + linkDiscord;
    }


    private static String getString(BanIP banIP) {
        String expireAt = "§aIP banido até: " + Util.color3 + banIP.getExpire_atFormated();
        String reason = "§aMotivo: " + Util.color3 + banIP.getReason();
        String bannedAt = "§aData do banimento: " + Util.color3 + banIP.getBanned_atFormated();

        return Util.title + "\n\n"
                + expireAt + "\n"
                + reason + "\n"
                + bannedAt + "\n"
                + "§aDiscord: " + Util.color3 + linkDiscord;
    }
}
