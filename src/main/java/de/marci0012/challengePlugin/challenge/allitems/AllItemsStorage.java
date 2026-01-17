package de.marci0012.challengePlugin.challenge.allitems;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

public class AllItemsStorage {

    private final File file;
    private final FileConfiguration config;

    public AllItemsStorage(JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "allitems.yml");
        if (!file.exists()) {
            plugin.saveResource("allitems.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public Set<Material> load() {
        Set<Material> items = EnumSet.noneOf(Material.class);
        if (config.contains("found")) {
            for (String name : config.getStringList("found")) {
                try {
                    items.add(Material.valueOf(name));
                } catch (IllegalArgumentException ignored) {}
            }
        }
        return items;
    }

    public void save(Set<Material> items) {
        config.set("found", items.stream().map(Enum::name).toList());
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
