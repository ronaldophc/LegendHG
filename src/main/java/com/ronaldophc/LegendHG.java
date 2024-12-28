package com.ronaldophc;

import java.sql.SQLException;
import java.util.logging.Logger;

import com.ronaldophc.hook.ProtocolLibHook;
import com.ronaldophc.feature.Border;
import com.ronaldophc.feature.Feast;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.ronaldophc.database.CurrentGameSQL;
import com.ronaldophc.database.GamesSQL;
import com.ronaldophc.database.MySQLManager;
import com.ronaldophc.gamestate.CountDown;
import com.ronaldophc.gamestate.GameStateManager;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.kits.manager.cooldowns.CooldownKits;
import com.ronaldophc.kits.manager.cooldowns.CooldownKits2;
import com.ronaldophc.kits.manager.kits.gladiator.GladiatorController;
import com.ronaldophc.register.RegisterCommands;
import com.ronaldophc.register.RegisterEvents;
import com.ronaldophc.register.RegisterKitsEvents;
import com.ronaldophc.feature.scoreboard.Board;
import com.ronaldophc.setting.Debug;
import com.ronaldophc.setting.Settings;
import com.ronaldophc.task.MainTask;

public class LegendHG extends JavaPlugin {

    public static final Logger logger = Logger.getLogger(LegendHG.class.getName());
    public GameStateManager gameStateManager;
    public KitManager kitManager;
    private BukkitTask mainTask;
    private BukkitTask boardTask;
    private BukkitTask countDownTask;
    private BukkitTask cooldownKits;
    private BukkitTask cooldownKits2;
    private MySQLManager mySQLManager;
    private GladiatorController gladiatorController;
    public Feast feast;
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

        mySQLManager = new MySQLManager();

        if (LegendHG.getMySQLManager().isActive()) {
            try {
                mySQLManager.initializeDatabase();
                CurrentGameSQL.deleteAllCurrentGameStats();
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

        gameStateManager = new GameStateManager(this);
        kitManager = new KitManager();
        countDownTask = getServer().getScheduler().runTaskTimer(this, CountDown.getInstance(), 0, 20);
        mainTask = getServer().getScheduler().runTaskTimer(this, MainTask.getInstance(), 0, 20);
        boardTask = getServer().getScheduler().runTaskTimer(this, Board.getInstance(), 0, 19);
        cooldownKits = getServer().getScheduler().runTaskTimer(this, CooldownKits.getInstance(), 0, 20);
        cooldownKits2 = getServer().getScheduler().runTaskTimer(this, CooldownKits2.getInstance(), 0, 20);
        feast = new Feast();

        Border.setWorldBorder();

        gladiatorController = new GladiatorController();

        logger.info("LegendHG enabled");
    }

    @Override
    public void onDisable() {
        if (boardTask != null) {
            boardTask.cancel();
        }
        if (countDownTask != null) {
            countDownTask.cancel();
        }
        if (mainTask != null) {
            mainTask.cancel();
        }
        if (cooldownKits != null) {
            cooldownKits.cancel();
        }
        if (cooldownKits2 != null) {
            cooldownKits2.cancel();
        }
        logger.info("LegendHG disabled");
    }

    public static LegendHG getInstance() {
        return getPlugin(LegendHG.class);
    }

    public static int getGameId() {
        return gameId;
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

    public static CooldownKits getCooldownKits() {
        return CooldownKits.getInstance();
    }

    public static CooldownKits2 getCooldownKits2() {
        return CooldownKits2.getInstance();
    }

    public static GladiatorController getGladiatorController() {
        return getInstance().gladiatorController;
    }

    public static Feast getFeast() {
        return getInstance().feast;
    }


}
