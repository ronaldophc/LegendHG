package com.ronaldophc;

import com.ronaldophc.api.border.BorderAPI;
import com.ronaldophc.api.scoreboard.Board;
import com.ronaldophc.database.GameRepository;
import com.ronaldophc.database.MySQLManager;
import com.ronaldophc.feature.CustomYaml;
import com.ronaldophc.feature.FeastManager;
import com.ronaldophc.feature.battleonthesummit.SummitManager;
import com.ronaldophc.feature.punish.PunishManager;
import com.ronaldophc.game.GameStateManager;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.kits.registry.gladiator.GladiatorController;
import com.ronaldophc.listener.Motd;
import com.ronaldophc.listener.ProtocolLibListener;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.register.RegisterCommands;
import com.ronaldophc.register.RegisterEvents;
import com.ronaldophc.register.RegisterKitsEvents;
import com.ronaldophc.task.FastTask;
import com.ronaldophc.task.NormalTask;
import com.ronaldophc.util.Helper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.SQLException;
import java.util.logging.Logger;

public class LegendHG extends JavaPlugin {

    public static Logger logger;
    public GameStateManager gameStateManager;
    public KitManager kitManager;
    private MySQLManager mySQLManager;
    private GladiatorController gladiatorController;

    private BukkitTask fastTask;
    private BukkitTask normalTask;
    PunishManager punishManager;

    public FeastManager feast;
    private Board board;

    public static CustomYaml settings;
    public static CustomYaml debug;
    public static CustomYaml messages;

    public boolean started = false;
    public boolean devMode = false;

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

        settings = new CustomYaml("settings");
        debug = new CustomYaml("debug");
        messages = new CustomYaml("messages");

        if (settings.getString("Environment").equalsIgnoreCase("dev")) {
            devMode = true;
        }

        ProtocolLibListener.register();
        logger.info("ProtocolLib is enabled.");

        Bukkit.getPluginManager().registerEvents(new Motd(), getInstance());

        if (!devMode) {
            logger.info("Loading Chunks -400 | 400");
            Helper.killEntitysNewVersion();
            Helper.loadChunks(-400, 400, -400, 400);
        }

        kitManager = new KitManager();
        mySQLManager = new MySQLManager();

        if (MySQLManager.isActive()) {
            try {
                mySQLManager.initializeDatabase();
                gameId = GameRepository.createGame();
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
        fastTask = getServer().getScheduler().runTaskTimer(this, FastTask.getInstance(), 0, 10);
        feast = new FeastManager();
        gladiatorController = new GladiatorController();

        BorderAPI.setWorldBorder();
        SummitManager.getInstance().initialize();

        normalTask = getServer().getScheduler().runTaskTimer(this, NormalTask.getInstance(), 0, 20);

        punishManager = new PunishManager();
        punishManager.startUnbanTask();

        started = true;
        logger.info("LegendHG enabled");
    }

    @Override
    public void onDisable() {
        Runtime.getRuntime().addShutdownHook(new Thread(punishManager::stopPunishTask));

        if (normalTask != null) {
            normalTask.cancel();
        }

        if (fastTask != null) {
            fastTask.cancel();
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
