package com.ronaldophc;

import com.ronaldophc.api.border.BorderAPI;
import com.ronaldophc.api.cooldown.CooldownAPI;
import com.ronaldophc.api.scoreboard.Board;
import com.ronaldophc.database.GameSQL;
import com.ronaldophc.database.MySQLManager;
import com.ronaldophc.feature.FeastManager;
import com.ronaldophc.feature.battleonthesummit.SummitManager;
import com.ronaldophc.game.CountDown;
import com.ronaldophc.game.GameStateManager;
import com.ronaldophc.hook.ProtocolLibHook;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.kits.registry.gladiator.GladiatorController;
import com.ronaldophc.listener.Motd;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.register.RegisterCommands;
import com.ronaldophc.register.RegisterEvents;
import com.ronaldophc.register.RegisterKitsEvents;
import com.ronaldophc.setting.Debug;
import com.ronaldophc.setting.Settings;
import com.ronaldophc.task.MainTask;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.SQLException;
import java.util.logging.Logger;

public class LegendHG extends JavaPlugin {

    public static Logger logger;
    public GameStateManager gameStateManager;
    public KitManager kitManager;
    private BukkitTask mainTask;
    private BukkitTask countDownTask;
    private BukkitTask cooldownKits;
    private MySQLManager mySQLManager;
    private GladiatorController gladiatorController;
    public FeastManager feast;
    private Board board;
    public boolean started = false;
    @Getter
    private static int gameId;

    @Override
    public void onLoad() {
        logger = getLogger();
        logger.info("LegendHG loaded");
    }

    @Override
    public void onEnable() {
        logger.info("LegendHG enabling");

        Settings.getInstance().load();
        Debug.getInstance().load();

        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            ProtocolLibHook.register();
            logger.info("ProtocolLib is enabled.");
        }

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Motd(), getInstance());

        logger.info("Loading Chunks -400 | 400");
//        Helper.killEntitysNewVersion();
//        Helper.loadChunks(-400, 400, -400, 400);

        kitManager = new KitManager();
        mySQLManager = new MySQLManager();

        if (MySQLManager.isActive()) {
            try {
                mySQLManager.initializeDatabase();
                gameId = GameSQL.createGame();
                logger.info("Game ID: " + gameId);
            } catch (SQLException e) {
                logger.info("Could not initialize database.");
            }
        }

        RegisterCommands.registerCommands();
        RegisterEvents.registerEvents();
        RegisterEvents.registerRecipes();
        RegisterKitsEvents.registerEvents();

        gameStateManager = new GameStateManager();
        board = new Board();
        countDownTask = getServer().getScheduler().runTaskTimer(this, CountDown.getInstance(), 0, 20);
        mainTask = getServer().getScheduler().runTaskTimer(this, MainTask.getInstance(), 0, 10);
        cooldownKits = getServer().getScheduler().runTaskTimer(this, CooldownAPI.getInstance(), 0, 20);
        feast = new FeastManager();
        gladiatorController = new GladiatorController();

        BorderAPI.setWorldBorder();
        SummitManager.getInstance().initialize();
        started = true;
        logger.info("LegendHG enabled");
    }

    @Override
    public void onDisable() {
        if (countDownTask != null) {
            countDownTask.cancel();
        }
        if (mainTask != null) {
            mainTask.cancel();
        }
        if (cooldownKits != null) {
            cooldownKits.cancel();
        }
        AccountManager.getInstance().getAccounts().forEach(Account::quitPlayer);
        logger.info("LegendHG disabled");
    }

    public static Board getBoard() {
        return getInstance().board;
    }

    public static LegendHG getInstance() {
        return getPlugin(LegendHG.class);
    }

    public static MySQLManager getMySQLManager() {
        return getInstance().mySQLManager;
    }

    public static GameStateManager getGameStateManager() {
        return getInstance().gameStateManager;
    }

    public static KitManager getKitManager() {
        return getInstance().kitManager;
    }

    public static GladiatorController getGladiatorController() {
        return getInstance().gladiatorController;
    }

    public static FeastManager getFeast() {
        return getInstance().feast;
    }

}
