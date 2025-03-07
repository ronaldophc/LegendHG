package com.ronaldophc.feature;

import com.ronaldophc.LegendHG;
import com.ronaldophc.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class CustomYaml {

    private final YamlConfiguration yamlConfiguration;
    public Plugin plugin = LegendHG.getInstance();
    private final File file;
    private final String fileName;

    public CustomYaml(String name) {
        this.fileName = name + ".yml";
        this.file = new File(plugin.getDataFolder(), fileName);
        this.yamlConfiguration = new YamlConfiguration();
        load();
    }

    private void load() {
        if (!this.file.exists()) {
            this.plugin.saveResource(this.fileName, false);
        }

        try {
            this.yamlConfiguration.load(file);
            LegendHG.logger.info("Loaded " + this.yamlConfiguration + " successfully.");
            debugYamlConfiguration();
        } catch (Exception e) {
            Logger.logError("Failed to load " + this.yamlConfiguration.getName() + ". " + e);
        }
    }

    private void debugYamlConfiguration() {
        Set<String> keys = this.yamlConfiguration.getKeys(true);
        for (String key : keys) {
            Object value = this.yamlConfiguration.get(key);
            LegendHG.logger.info("Key: " + key + ", Value: " + value);
        }
    }

    public void save() {
        try {
            this.yamlConfiguration.save(file);
        } catch (IOException e) {
            Logger.logError("Failed to save " + this.yamlConfiguration.getName() + ". " + e);
        }
    }

    public boolean exist(String path) {
        return this.yamlConfiguration.isSet(path);
    }

    public void set(String path, Object value) {
        this.yamlConfiguration.set(path, value);
        save();
    }

    public int getInt(String path) {
        if (!(this.yamlConfiguration.isSet(path))) {
            Logger.logError("Failed to get yamlConfiguration " + path + " in " + this.yamlConfiguration.getName() + ", shutdown the server!");
            Bukkit.shutdown();
        }
        return this.yamlConfiguration.getInt(path);
    }

    public String getString(String path) {
        if (!(yamlConfiguration.isSet(path))) {
            Logger.logError("Failed to get yamlConfiguration " + path + " in " + this.yamlConfiguration.getName() + ", shutdown the server!");
            Bukkit.shutdown();
        }
        return this.yamlConfiguration.getString(path);
    }

    public String getAutoMessage(int id) {
        String path = "Messages." + id;
        if (!(this.yamlConfiguration.isSet(path))) {
            return null;
        }
        return this.yamlConfiguration.getString(path);
    }

    public boolean getBoolean(String path) {
        if (!(this.yamlConfiguration.isSet(path))) {
            Logger.logError("Failed to get yamlConfiguration " + path + " in " + this.yamlConfiguration.getName() + ", shutdown the server!");
            Bukkit.shutdown();
        }
        return this.yamlConfiguration.getBoolean(path);
    }

}