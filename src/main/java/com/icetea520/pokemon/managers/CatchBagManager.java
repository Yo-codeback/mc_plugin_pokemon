package com.icetea520.pokemon.managers;

import com.icetea520.pokemon.PokemonPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;

public class CatchBagManager {
    
    private final PokemonPlugin plugin;
    private final Map<UUID, List<CaughtPokemon>> playerBags;
    private static final int MAX_BAG_SIZE = 100;
    
    public CatchBagManager(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.playerBags = new HashMap<>();
    }
    
    public boolean addPokemon(Player player, Entity entity) {
        UUID playerId = player.getUniqueId();
        List<CaughtPokemon> bag = playerBags.getOrDefault(playerId, new ArrayList<>());
        
        if (bag.size() >= MAX_BAG_SIZE) {
            return false; // 背包已滿
        }
        
        CaughtPokemon pokemon = new CaughtPokemon(
            entity.getType().name(),
            entity.getName(),
            System.currentTimeMillis()
        );
        
        bag.add(pokemon);
        playerBags.put(playerId, bag);
        return true;
    }
    
    public List<CaughtPokemon> getPlayerBag(Player player) {
        return playerBags.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }
    
    public int getBagSize(Player player) {
        return getPlayerBag(player).size();
    }
    
    public static class CaughtPokemon {
        private final String type;
        private final String name;
        private final long catchTime;
        
        public CaughtPokemon(String type, String name, long catchTime) {
            this.type = type;
            this.name = name;
            this.catchTime = catchTime;
        }
        
        public String getType() { return type; }
        public String getName() { return name; }
        public long getCatchTime() { return catchTime; }
    }
}
