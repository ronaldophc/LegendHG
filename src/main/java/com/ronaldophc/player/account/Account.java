package com.ronaldophc.player.account;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.CooldownType;
import com.ronaldophc.constant.MCVersion;
import com.ronaldophc.constant.MySQL.PlayerField;
import com.ronaldophc.constant.MySQL.Tables;
import com.ronaldophc.constant.Scores;
import com.ronaldophc.constant.Tags;
import com.ronaldophc.database.MySQLManager;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kits;
import com.viaversion.viaversion.api.Via;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
public class Account {

    private Player player;
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
    private MCVersion version;
    private boolean tell;
    private boolean chat;
    private int wins;
    private Scores score;
    private boolean build;
    private CooldownType cooldownType;

    public Account(Player player) {
        this.player = player;
        this.UUID = player.getUniqueId();
        this.kits = new Kits();
        this.originalName = player.getName();
        this.actualName = player.getName();
        this.ip = player.getAddress().getAddress().getHostAddress();
        this.loggedIn = false;
        this.alive = false;
        this.spectator = false;
        this.vanish = false;
        this.kills = 0;
        this.build = false;
        initializeTag();
        initializeTell();
        initializeChat();
        initializeWins();
        initializeScore();
        initializeVersion();
        initializeCooldownType();
    }

    public boolean login(String password) {
        try {
            return PlayerSQL.loginPlayer(player, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeCooldownType() {
        try {
            this.cooldownType = PlayerSQL.getPlayerCooldownType(player);
        } catch (SQLException e) {
            Logger.logError("Erro ao setar as cooldown_type do profile do jogador: " + e.getMessage());
        }
    }

    public void initializeWins() {
        try {
            this.wins = MySQLManager.getInt(UUID.toString(), Tables.PLAYER.getTableName(), PlayerField.WINS.getFieldName());
        } catch (SQLException e) {
            Logger.logError("Erro ao setar as wins do profile do jogador: " + e.getMessage());
        }
    }

    public void initializeScore() {
        try {
            this.score = PlayerSQL.getPlayerScore(player);
        } catch (SQLException e) {
            Logger.logError("Erro ao setar as wins do profile do jogador: " + e.getMessage());
        }
    }

    public void initializeChat() {
        try {
            this.chat =  MySQLManager.getBoolean(UUID.toString(), Tables.PLAYER.getTableName(), PlayerField.CHAT.getFieldName());
        } catch (SQLException e) {
            Logger.logError("Erro ao setar o chat do profile do jogador: " + e.getMessage());
        }
    }

    public void initializeTell() {
        try {
            tell = MySQLManager.getBoolean(UUID.toString(), Tables.PLAYER.getTableName(), PlayerField.TELL.getFieldName());
        } catch (SQLException e) {
            Logger.logError("Erro ao setar o tell do profile do jogador: " + e.getMessage());
        }
    }

    public void initializeTag() {
        try {
            this.tag = PlayerSQL.getPlayerTag(player);
        } catch (SQLException e) {
            Logger.logError("Erro ao setar o tag do profile do jogador: " + e.getMessage());
        }
    }

    public void initializeVersion() {
        new BukkitRunnable() {

            @Override
            public void run() {
                int viaVersion = Via.getAPI().getPlayerVersion(player.getUniqueId());
                if (viaVersion <= MCVersion.MC_1_7.getProtocolVersion()) {
                    version = MCVersion.MC_1_7;
                    return;
                }
                version = MCVersion.MC_1_8;
            }

        }.runTaskLater(LegendHG.getInstance(), 20);
    }

    public void addKill() {
        this.kills++;
        try {
            MySQLManager.setInt(UUID.toString(), Tables.PLAYER.getTableName(), PlayerField.KILLS.getFieldName(), getTotalKills() + 1);
        } catch (SQLException error) {
            Logger.logError("Erro ao atualizar informações do jogador: " + error.getMessage());
        }
    }

    public void logout() {
        this.loggedIn = false;
        try {
            PlayerSQL.logoutPlayer(player);
        } catch (SQLException e) {
            Logger.logError("Erro ao deslogar jogador: " + e.getMessage());
        }
    }

    public void setChat(Boolean chat) {
        this.chat = chat;
        try {
            MySQLManager.setBoolean(UUID.toString(), Tables.PLAYER.getTableName(), PlayerField.CHAT.getFieldName(), chat);
        } catch (SQLException error) {
            Logger.logError("Erro ao atualizar informações do jogador: " + error.getMessage());
        }
    }

    public void setTell(Boolean tell) {
        this.tell = tell;
        try {
            MySQLManager.setBoolean(UUID.toString(), Tables.PLAYER.getTableName(), PlayerField.TELL.getFieldName(), tell);
        } catch (SQLException error) {
            Logger.logError("Erro ao atualizar informações do jogador: " + error.getMessage());
        }
    }

    public void setCooldownType(CooldownType cooldown_type) {
        this.cooldownType = cooldown_type;
        try {
            PlayerSQL.setPlayerCooldownType(player, cooldown_type);
        } catch (SQLException error) {
            Logger.logError("Erro ao atualizar informações do jogador: " + error.getMessage());
        }
    }

    public void setScore(Scores score) {
        try {
            this.score = score;
            PlayerSQL.setPlayerScore(player, score);
        } catch (SQLException e) {
            Logger.logError("Erro ao recuperar Score do jogador: " + e.getMessage());
        }
    }

    public void setTag(Tags tag) {
        try {
            this.tag = tag;
            PlayerSQL.setPlayerTag(player, tag);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getTotalKills() {
        try {
            return MySQLManager.getInt(UUID.toString(), Tables.PLAYER.getTableName(), PlayerField.KILLS.getFieldName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void quitPlayer() {
        if (player.isOnline()) {
            player.kickPlayer(Util.title + "\n\nServidor reiniciando, voltamos em breve!");
        }
    }
}
