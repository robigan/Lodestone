package com.robigan.lodestone;

import me.NoChance.PvPManager.PvPManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public final class Lodestone extends JavaPlugin {
    @Nullable
    private static PvPManager pvpInstance;

    @Override
    public void onEnable() {
        pvpInstance = (PvPManager) Bukkit.getPluginManager().getPlugin("PvPManager");

        Bukkit.getLogger().info("Listening to Lodestone Compass usage");
        Bukkit.getServer().getPluginManager().registerEvents(new CompassListener(), this);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Shutting down listening to Lodestone Compass usage");
    }

    @Nullable
    public static PvPManager getPvpInstance() {
        return pvpInstance;
    }
}
