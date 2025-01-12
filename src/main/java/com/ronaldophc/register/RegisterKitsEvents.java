package com.ronaldophc.register;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.Modifier;
import java.util.Set;

public class RegisterKitsEvents {

    public static void registerEvents() {
        registerAllKits(LegendHG.getInstance());
    }

    private static void registerAllKits(JavaPlugin plugin) {
        LegendHG.logger.info("Registering kits...");
        try {
            Reflections reflections = new Reflections("com.ronaldophc.kits.registry", Scanners.SubTypes);
            Set<Class<? extends Kit>> kitClasses = reflections.getSubTypesOf(Kit.class);
            KitManager kitManager = LegendHG.getKitManager();

            for (Class<? extends Kit> clazz : kitClasses) {
                if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
                    Kit kit = clazz.getDeclaredConstructor().newInstance();

                    if (kitManager == null) {
                        LegendHG.logger.severe("KitManager is null");
                        continue;
                    }

                    kitManager.registerKit(kit, plugin);
                }
            }

            kitManager.sortKits();
            LegendHG.logger.info("Kits registered successfully");
        } catch (Exception e) {
            LegendHG.logger.severe("Error registering kits: " + e.getMessage());
        }

    }
}