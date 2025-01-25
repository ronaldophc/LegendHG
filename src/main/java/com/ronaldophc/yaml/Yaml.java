package com.ronaldophc.yaml;

import com.ronaldophc.LegendHG;
import com.ronaldophc.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Yaml {

    private final YamlConfiguration yamlConfiguration;
    public static Plugin plugin = LegendHG.getInstance();

    public Yaml(YamlConfiguration yamlConfiguration) {
        this.yamlConfiguration = yamlConfiguration;
    }

    public void save() {
        try {
            yamlConfiguration.save(new File(plugin.getDataFolder(), yamlConfiguration.getName()));
        } catch (Exception e) {
            Logger.logError("Failed to save Settings.YML!");
        }
    }

    public boolean exist(String path) {
        return yamlConfiguration.isSet(path);
    }

    public void set(String path, Object value) {
        yamlConfiguration.set(path, value);
        save();
    }

    public int getInt(String path) {
        if (!(yamlConfiguration.isSet(path))) {
            Logger.logError("Failed to get yamlConfiguration " + path + " in " + yamlConfiguration.getName() + ", shutdown the server!");
            Bukkit.shutdown();
        }
        return yamlConfiguration.getInt(path);
    }

    public String getString(String path) {
        if (!(yamlConfiguration.isSet(path))) {
            Logger.logError("Failed to get yamlConfiguration " + path + " in " + yamlConfiguration.getName() + ", shutdown the server!");
            Bukkit.shutdown();
        }
        return yamlConfiguration.getString(path);
    }

    public String getAutoMessage(int id) {
        String path = "Messages." + id;
        if (!(yamlConfiguration.isSet(path))) {
            return null;
        }
        return yamlConfiguration.getString(path);
    }

}
