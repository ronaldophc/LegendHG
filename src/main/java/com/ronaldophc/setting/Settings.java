package com.ronaldophc.setting;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Settings {

    private final static Settings instance = new Settings();
    private File file;
    private YamlConfiguration config;

    private Settings() {
    }

    public void load() {
        file = new File(LegendHG.getInstance().getDataFolder(), "settings.yml");

        if (!file.exists())
            LegendHG.getInstance().saveResource("settings.yml", false);

        config = YamlConfiguration.loadConfiguration(file);

    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean exist(String path) {
        return config.isSet(path);
    }

    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    public int getInt(String path) {
        if (!(config.isSet(path))) {
            Logger.logError("Failed to get config " + path + " in Settings.YML, shutdown the server!");
            Bukkit.shutdown();
        }
        return config.getInt(path);
    }

    public String getString(String path) {
        if (!(config.isSet(path))) {
            Logger.logError("Failed to get config " + path + " in Settings.YML, shutdown the server!");
            Bukkit.shutdown();
        }
        return config.getString(path);
    }

    public static Settings getInstance() {
        return instance;
    }
}
