package com.robigan.lodestone;

import org.bukkit.plugin.java.JavaPlugin;

public final class Lodestone extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getLogger().info("Listening to Lodestone Compass usage");
        this.getServer().getPluginManager().registerEvents(new CompassListener(), this);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Shutting down listening to Lodestone Compass usage");
    }
}
