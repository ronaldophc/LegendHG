package com.ronaldophc.player.account;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Tags;
import com.ronaldophc.database.CurrentGameSQL;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.feature.TagManager;
import com.ronaldophc.feature.auth.AuthManager;
import com.ronaldophc.kits.Kits;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class Account {

    private final Player player;
    private final UUID uuid;
    private Tags tag;
    private volatile boolean loggedIn = false;
    private final Kits kits;
    private boolean spectator = false;
    private final String originalName;
    private String actualName;

    public Account(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.tag = TagManager.getTagSQL(player);
        this.kits = new Kits();
        this.originalName = player.getName();
        this.actualName = player.getName();
    }

    public UUID getUUID() {
        return uuid;
    }

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Kits getKits() {
        return kits;
    }

    public boolean isSpectator() {
        return spectator;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }

    public void setActualName(String actualName) {
        this.actualName = actualName;
    }

    public String getOriginalName() {
        return this.originalName;
    }

    public String getActualName() {
        return this.actualName;
    }

    public boolean loginPlayer(String password) throws SQLException {
        if (PlayerSQL.loginPlayer(this.player, password)) {
            setLoggedIn(true);
            AuthManager.loginPlayer(player);
            CurrentGameSQL.createCurrentGameStats(player, LegendHG.getGameId());
            return true;
        }
        return false;
    }
}
