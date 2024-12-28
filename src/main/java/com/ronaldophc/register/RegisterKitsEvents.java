package com.ronaldophc.register;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class RegisterKitsEvents extends LegendHG {

    public static void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Stomper(), getInstance());
        pm.registerEvents(new Viper(), getInstance());
        pm.registerEvents(new Endermage(), getInstance());
        pm.registerEvents(new Fisherman(), getInstance());
        pm.registerEvents(new Miner(), getInstance());
        pm.registerEvents(new Ninja(), getInstance());
        pm.registerEvents(new Gladiator(), getInstance());
        pm.registerEvents(new Thor(), getInstance());
        pm.registerEvents(new Gravity(), getInstance());
        pm.registerEvents(new Achilles(), getInstance());
        pm.registerEvents(new Anchor(), getInstance());
        pm.registerEvents(new Berserker(), getInstance());
        pm.registerEvents(new Barbarian(), getInstance());
        pm.registerEvents(new Blink(), getInstance());
        pm.registerEvents(new Fireman(), getInstance());
        pm.registerEvents(new Flash(), getInstance());
        pm.registerEvents(new Forger(), getInstance());
        pm.registerEvents(new Kangaroo(), getInstance());
        pm.registerEvents(new Specialist(), getInstance());
        pm.registerEvents(new Boxer(), getInstance());
        pm.registerEvents(new Switcher(), getInstance());
        pm.registerEvents(new Monk(), getInstance());
        pm.registerEvents(new Phantom(), getInstance());
        pm.registerEvents(new Digger(), getInstance());
        pm.registerEvents(new Launcher(), getInstance());
        pm.registerEvents(new Viking(), getInstance());
        pm.registerEvents(new Cannibal(), getInstance());
        pm.registerEvents(new Camel(), getInstance());
        pm.registerEvents(new JackHammer(), getInstance());
        pm.registerEvents(new Thermo(), getInstance());
        pm.registerEvents(new Cultivator(), getInstance());
        pm.registerEvents(new Tank(), getInstance());
        pm.registerEvents(new Pyro(), getInstance());
        pm.registerEvents(new Titan(), getInstance());
        pm.registerEvents(new Brand(), getInstance());
        pm.registerEvents(new IronMan(), getInstance());
        pm.registerEvents(new Magma(), getInstance());
        pm.registerEvents(new Popai(), getInstance());
        pm.registerEvents(new Timerlord(), getInstance());
    }
}
