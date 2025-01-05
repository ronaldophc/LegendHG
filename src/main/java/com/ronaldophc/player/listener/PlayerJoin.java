package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.feature.SkinManager;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.MasterHelper;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.player.account.Account;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();

        UUID uuid = player.getUniqueId();
        Account account = LegendHG.getAccountManager().getOrCreateAccount(player);

        if (!(player.isOp())) {
            MasterHelper.injectPlayerNotTabComplete(player);
        }

        try {
            if (SkinManager.playerSkin.containsKey(uuid)) {
                SkinManager.changePlayerSkin(player, SkinManager.playerSkin.get(uuid));
            } else {
                SkinManager.fixPlayerSkin(player);
            }
            MasterHelper.refreshPlayer(player);
        } catch (Exception e) {
            Logger.logError("Erro ao alterar skin ou tag: " + e.getMessage());
            throw new RuntimeException(e);
        }

        if (!account.isSpectator()) {
            PlayerHelper.teleportPlayerToSpawnLocation(player);
        }

        event.setJoinMessage(Util.color2 + "[+] " + Util.color1 + account.getActualName());

        if (account.isLoggedIn()) {
            return;
        }

        String message = Util.title + " > " + Util.usage("/register <password>");

        if (PlayerSQL.isPlayerRegistered(player)) {
            message = Util.title + " > " + Util.usage("/login <password>");
        }

        player.sendMessage(message);
    }

}