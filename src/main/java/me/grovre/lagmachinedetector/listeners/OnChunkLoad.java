package me.grovre.lagmachinedetector.listeners;

import me.grovre.lagmachinedetector.LagMachineDetector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.HashMap;
import java.util.Map;

public class OnChunkLoad implements Listener {

    @EventHandler
    public void onServerLoadChunk(ChunkLoadEvent event) {
        // Plugin and config
        LagMachineDetector plugin = LagMachineDetector.getPlugin();
        FileConfiguration config = plugin.getConfig();

        Chunk c = event.getChunk();
        // Runs later to make sure the entities are loaded before running. If they're not, prints to the console
        // that entities need to be loaded for this work and to raise the value in the config.
        Bukkit.getScheduler().runTaskLater(plugin, t ->
        {
            // Makes sure the entities are loaded
            if (!c.isEntitiesLoaded()) {
                System.out.println("Checked before entities were loaded. Raise CheckDelay value in config to prevent ");
                return;
            }

            // Fills entityMap with types and how many there are
            Map<EntityType, Integer> entityMap = new HashMap<>();
            for(Entity e : c.getEntities()) {
                entityMap.merge(e.getType(), 1, Integer::sum);
            }

            // Gets the total entity count and sends an alert if the total entity count is above the threshold
            int totalEntityCount = c.getEntities().length;
            int totalEntityWarningThreshold = config.getInt("TotalEntityWarningThreshold");
            if(totalEntityCount > totalEntityWarningThreshold) {
                String alertMessage = "Chunk entity warning! There are " + totalEntityCount + " total entities in chunk at (chunk coords) " + c.getX() + ", " + c.getZ() + "! Visit the chunk with /lagg tpchunk [x] [z]";
                System.out.println(alertMessage);
                sendAlertToPermitted(alertMessage, "LagDetector.getAlert");
            }

            // Sends alert to permitted if a specific entity type's count from the map is above the threshold
            for(EntityType key : entityMap.keySet()) {
                if(entityMap.get(key) > config.getInt("EntityTypeWarningThreshold")) {
                    String alertMessage = "Chunk entity warning! There are " + entityMap.get(key) + " " + key.name() + " at (chunk coords) " + c.getX() + ", " + c.getZ() + "! Visit the chunk with /lagg tpchunk [x] [z]";
                    System.out.println(alertMessage);
                    sendAlertToPermitted(alertMessage, "LagDetector.getAlert");
                }
            }
            // Uses the delay
        }, config.getInt("CheckDelay"));
    }

    public void sendAlertToPermitted(String message, String permission) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.hasPermission(permission)) {
                player.sendMessage(ChatColor.RED + message);
            }
        }
    }
}
