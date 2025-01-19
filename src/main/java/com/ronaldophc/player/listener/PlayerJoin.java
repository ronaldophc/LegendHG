package com.ronaldophc.player.listener;

import com.ronaldophc.api.skin.SkinAPI;
import com.ronaldophc.api.title.TitleAPI;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.util.Helper;
import com.ronaldophc.util.Logger;
import com.ronaldophc.util.Util;
import com.ronaldophc.player.PlayerHelper;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();

        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        UUID uuid = account.getUUID();

        if (!(player.isOp())) {
            Helper.injectPlayerNotTabComplete(player);
        }

        try {
            if (SkinAPI.playerSkin.containsKey(uuid)) {
                SkinAPI.changePlayerSkin(player, SkinAPI.playerSkin.get(uuid));
            } else {
                SkinAPI.fixPlayerSkin(player);
            }
            Helper.refreshPlayer(player);
        } catch (Exception e) {
            Logger.logError("Erro ao alterar skin ou tag: " + e.getMessage());
            throw new RuntimeException(e);
        }

        if (!account.isSpectator() && !account.isAlive()) {
            player.getInventory().clear();
            player.updateInventory();
            PlayerHelper.teleportPlayerToSpawnLocation(player);
        }

        event.setJoinMessage(Util.color3 + account.getActualName() + " entrou!");

        if (account.isLoggedIn()) {
            return;
        }

        String message = Util.title + " > " + Util.usage("/register <password>");
        String title = Util.usage("/register");

        if (PlayerSQL.isPlayerRegistered(player)) {
            message = Util.title + " > " + Util.usage("/login <password>");
            title = Util.usage("/login");
        }
        TitleAPI.setTitle(player, title, "Seja Bem-Vindo!", 10, 40, 10);
        player.sendMessage(message);
    }

}