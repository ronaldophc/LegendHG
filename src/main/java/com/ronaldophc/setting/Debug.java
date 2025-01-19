package com.ronaldophc.setting;

import com.ronaldophc.LegendHG;
import com.ronaldophc.util.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Debug {

    private final static Debug instance = new Debug();
    private File file;
    private YamlConfiguration config;

    private Debug() {
    }

    public void load() {
        file = new File(LegendHG.getInstance().getDataFolder(), "debug.yml");

        if (!file.exists())
            LegendHG.getInstance().saveResource("debug.yml", false);

        config = YamlConfiguration.loadConfiguration(file);

    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public String getString(String path) {
        try {
            return config.getString(path);
        } catch (Exception e) {
            Logger.logError("Failed to get string from debug.yml: " + e.getMessage());
        }
        return "";
    }

    public static Debug getInstance() {
        return instance;
    }
}
