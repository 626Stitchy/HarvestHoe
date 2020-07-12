package com.anubis.HarvestHoe;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class AnubisHarvestHoe extends JavaPlugin {
    @Override
    public void onEnable() {
        registerConfig();
        saveDefaultConfig();
        registerCommands();
        registerEvents();
    }

    private void registerConfig()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void registerCommands()
    {
        getCommand("harvesthoe").setExecutor(new HarvestHoeCommand(this));
    }

    public void registerEvents()
    {
        getServer().getPluginManager().registerEvents(new BlockBreakEvents(this), this);
    }

    public String getConfigStringValue(String key) {
        return getConfig().get(key).toString();
    }

    public List<String> getConfigStringList(String key) {
        return getConfig().getStringList(key);
    }

    public String convertColors(String input) {
        return input.replaceAll("&", "§");
    }
}
