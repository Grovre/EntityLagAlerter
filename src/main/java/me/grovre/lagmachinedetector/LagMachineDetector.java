package me.grovre.lagmachinedetector;

import me.grovre.lagmachinedetector.listeners.OnChunkLoad;
import org.bukkit.plugin.java.JavaPlugin;

public final class LagMachineDetector extends JavaPlugin {

    public static LagMachineDetector plugin;

    public static LagMachineDetector getPlugin() {
        return plugin;
    }

    /*
    Permissions:
    LagDetector.getAlert
     */

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        plugin = this;

        getServer().getPluginManager().registerEvents(new OnChunkLoad(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
