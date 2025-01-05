package com.ronaldophc.player.account;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Scores;
import com.ronaldophc.constant.Tags;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.feature.TagManager;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.kits.Kits;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class Account {

    private final Player player;
    private final UUID UUID;
    private Tags tag;
    private volatile boolean loggedIn;
    private final Kits kits;
    private boolean spectator;
    private boolean alive;
    private boolean vanish;
    private final String originalName;
    private String actualName;
    private String ip;
    private int kills;
    private int version;

    public Account(Player player) {
        this.player = player;
        this.UUID = player.getUniqueId();
        this.tag = TagManager.getTagSQL(player);
        this.kits = new Kits();
        this.originalName = player.getName();
        this.actualName = player.getName();
        this.ip = player.getAddress().getAddress().getHostAddress();
        this.loggedIn = false;
        this.alive = false;
        this.spectator = false;
        this.vanish = false;
        this.kills = 0;
        setVersion();
    }

    public void setVersion() {
        new BukkitRunnable() {

            @Override
            public void run() {
                ViaAPI api = Via.getAPI();
                version = api.getPlayerVersion(player);
            }

        }.runTaskLater(LegendHG.getInstance(), 20);
    }

    public void addKill() throws SQLException {
        this.kills++;
    }

    public void logout() {
        this.loggedIn = false;
        try {
            PlayerSQL.logoutPlayer(player);
        } catch (SQLException e) {
            Logger.logError("Erro ao deslogar jogador: " + e.getMessage());
        }
    }

    public Scores getScore() {
        try {
            return PlayerSQL.getPlayerScore(player);
        } catch (SQLException e) {
            Logger.logError("Erro ao recuperar Score do jogador: " + e.getMessage());
        }
        return Scores.NONE;
    }
}
