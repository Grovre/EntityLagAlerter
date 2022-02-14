package me.grovre.lagmachinedetector.listeners;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.HashMap;
import java.util.Map;

public class OnChunkLoad implements Listener {

    @EventHandler
    public void onServerLoadChunk(ChunkLoadEvent event) {
        Chunk c = event.getChunk();
        if (!c.isEntitiesLoaded()) {
            System.out.println("Chunk entities not loaded... Send log to PoptartFromPluto");
        }
        System.out.println("Entities loaded!");

        // Fills entityMap with types and how many there are
        Map<EntityType, Integer> entityMap = new HashMap<>();
        for(Entity e : c.getEntities()) {
            EntityType k = e.getType(); // k is EntityType in entityMap
            if(entityMap.containsKey(k)) {
                entityMap.put(k, entityMap.get(k) + 1); // Sets EntityType key to previous value +1
            } else {
                entityMap.put(k, 1); // Sets amount of EntityType to 1
            }
        }

        int totalEntityCount = c.getEntities().length;
        if(totalEntityCount > 10) {
            System.out.println("Warning! Total entity count in chunk at " + c.getX() + ", " + c.getZ() + " is greater than 10!");
        }


    }

}
