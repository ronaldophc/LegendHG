package com.ronaldophc;

import com.ronaldophc.database.GamesSQL;
import com.ronaldophc.database.MySQLManager;
import com.ronaldophc.feature.BorderManager;
import com.ronaldophc.feature.FeastManager;
import com.ronaldophc.feature.scoreboard.Board;
import com.ronaldophc.gamestate.CountDown;
import com.ronaldophc.gamestate.GameStateManager;
import com.ronaldophc.hook.ProtocolLibHook;
import com.ronaldophc.kits.CooldownAPI;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.kits.registry.gladiator.GladiatorController;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.register.RegisterCommands;
import com.ronaldophc.register.RegisterEvents;
import com.ronaldophc.register.RegisterKitsEvents;
import com.ronaldophc.setting.Debug;
import com.ronaldophc.setting.Settings;
import com.ronaldophc.task.MainTask;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.sql.SQLException;
import java.util.logging.Logger;

public class LegendHG extends JavaPlugin {

    public static final Logger logger = Logger.getLogger(LegendHG.class.getName());
    public GameStateManager gameStateManager;
    public KitManager kitManager;
    private BukkitTask mainTask;
    private BukkitTask countDownTask;
    private BukkitTask cooldownKits;
    private AccountManager accountManager;
    private MySQLManager mySQLManager;
    private GladiatorController gladiatorController;
    public FeastManager feast;
    private Board board;
    @Getter
    private static int gameId;

    @Override
    public void onLoad() {
        logger.info("LegendHG loaded");
    }

    @Override
    public void onEnable() {
        Settings.getInstance().load();
        Debug.getInstance().load();

        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            ProtocolLibHook.register();
            System.out.println("ProtocolLib is enabled.");
        }

        kitManager = new KitManager();
        accountManager = new AccountManager();
        mySQLManager = new MySQLManager();

        if (LegendHG.getMySQLManager().isActive()) {
            try {
                mySQLManager.initializeDatabase();
                gameId = GamesSQL.createGame();
                System.out.println("Game ID: " + gameId);
            } catch (SQLException e) {
                System.out.println("Could not initialize database.");
            }
        }

        RegisterCommands.registerCommands();
        RegisterEvents.registerEvents();
        RegisterEvents.registerRecipes();
        RegisterKitsEvents.registerEvents();

        gameStateManager = new GameStateManager();
        board = new Board();
        countDownTask = getServer().getScheduler().runTaskTimer(this, CountDown.getInstance(), 0, 20);
        mainTask = getServer().getScheduler().runTaskTimer(this, MainTask.getInstance(), 0, 20);
        cooldownKits = getServer().getScheduler().runTaskTimer(this, CooldownAPI.getInstance(), 0, 20);
        feast = new FeastManager();
        gladiatorController = new GladiatorController();

        BorderManager.setWorldBorder();

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

    public static AccountManager getAccountManager() {
        return getInstance().accountManager;
    }

    public static GladiatorController getGladiatorController() {
        return getInstance().gladiatorController;
    }

    public static FeastManager getFeast() {
        return getInstance().feast;
    }

}
