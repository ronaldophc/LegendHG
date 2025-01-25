package com.ronaldophc.yaml;

import com.ronaldophc.LegendHG;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class YamlService {

    public static Plugin plugin = LegendHG.getInstance();

    public static Yaml loadYamlConfiguration(String name) {
        String fileName = name + ".yml";
        File file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        return new Yaml(yamlConfiguration);
    }


}
