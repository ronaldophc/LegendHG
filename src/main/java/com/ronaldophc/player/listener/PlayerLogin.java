package com.ronaldophc.player.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.setting.Settings;
import com.ronaldophc.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLogin implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        GameState gameState = LegendHG.getGameStateManager().getGameState();
        String permLogin = "legendhg.login.started";
        String permSpec = "legendhg.login.spectator";
        String permMax = "legendhg.login.maxplayers";
        int maxPlayers = Settings.getInstance().getInt("MaxPlayers");
        int playersOn = AccountManager.getInstance().getPlayersAlive().size();
        switch (gameState) {
            case COUNTDOWN:
                if (playersOn >= maxPlayers && (!player.hasPermission(permMax)) && !player.isOp())
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Util.title + "\n\n" + Util.success + Util.bold + "Apenas" + Util.color2 + Util.bold + " VIP " + Util.success + Util.bold + "pode entrar com o servidor cheio!!");
                break;

            case INVINCIBILITY:
                if (player.isOp()) return;
                if (player.hasPermission(permLogin)) return;
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Util.title + "\n\n" + Util.success + Util.bold + "Apenas" + Util.color2 + Util.bold + " VIP " + Util.success + Util.bold + "pode entrar depois que a partida iniciou!");
                break;

            default:
                if (player.isOp()) return;
                if (!player.hasPermission(permSpec)) {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Util.title + "\n\n" + Util.success + Util.bold + " O jogo já começou, para assistir se torne VIP!");
                }
                break;
        }
    }
}
