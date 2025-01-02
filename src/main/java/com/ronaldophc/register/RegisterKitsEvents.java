package com.ronaldophc.register;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.logging.Logger;

public class RegisterKitsEvents {

    private static final Logger logger = Logger.getLogger(RegisterKitsEvents.class.getName());

    public static void registerEvents() {
        registerAllKits(LegendHG.getInstance());
    }

    private static void registerAllKits(JavaPlugin plugin) {
        try {
            Reflections reflections = new Reflections("com.ronaldophc.kits.registry", Scanners.SubTypes);
            Set<Class<? extends Kit>> kitClasses = reflections.getSubTypesOf(Kit.class);

            for (Class<? extends Kit> clazz : kitClasses) {
                if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
                    Kit kit = clazz.getDeclaredConstructor().newInstance();

                    KitManager kitManager = LegendHG.getKitManager();
                    if (kitManager == null) {
                        logger.severe("KitManager is null");
                        continue;
                    }

                    kitManager.registerKit(kit, plugin);
                }
            }
        } catch (Exception e) {
            logger.severe("Error registering kits: " + e.getMessage());
        }
    }
}