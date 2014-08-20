package com.shdwlf.dumbxp;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class XPBank {

    private DumbXP plugin;
    private YamlConfiguration config;
    private String fileName;

    public XPBank(DumbXP plugin, String fileName) {
        this.plugin = plugin;
        this.config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + fileName));
        this.fileName = fileName;
    }

    public int getLevels(Player player) {
        return config.getInt("players." + player.getUniqueId().toString() + ".balance", 0);
    }

    public void setLevels(Player player, int levels) {
        if(plugin.getConfig().getBoolean("cache-username"))
            config.set("players." + player.getUniqueId().toString() + ".name", player.getName());
        else if(config.contains("players." + player.getUniqueId().toString() + ".name"))
            config.set("players." + player.getUniqueId().toString() + ".name", null);

        config.set("players." + player.getUniqueId().toString() + ".balance", levels);
    }

    public void addLevels(Player player, int levels) {
        if(levels >= 0)
            setLevels(player, getLevels(player) + levels);
    }

    public void removeLevels(Player player, int levels) {
        if(levels >= 0) {
            setLevels(player, getLevels(player) - levels);
        }
    }

    public void saveFile() throws IOException {
        config.save(plugin.getDataFolder() + File.separator + fileName);
    }
}
