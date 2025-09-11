package com.icetea520.pokemon.managers;

import com.icetea520.pokemon.PokemonPlugin;
import com.icetea520.pokemon.managers.CatchBagManager.CaughtPokemon;
import com.icetea520.pokemon.managers.SupplyManager.SupplyPoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataManager {
    
    private final PokemonPlugin plugin;
    private final File dataFolder;
    private final File playerDataFile;
    private final File supplyDataFile;
    
    public DataManager(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "data");
        this.playerDataFile = new File(dataFolder, "players.yml");
        this.supplyDataFile = new File(dataFolder, "supply_points.yml");
        
        // 確保資料夾存在
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }
    
    /**
     * 載入所有玩家資料
     */
    public Map<UUID, List<CaughtPokemon>> loadPlayerData() {
        Map<UUID, List<CaughtPokemon>> playerData = new HashMap<>();
        
        if (!playerDataFile.exists()) {
            return playerData;
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerDataFile);
        
        for (String playerId : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(playerId);
                List<CaughtPokemon> pokemonList = new ArrayList<>();
                
                ConfigurationSection playerSection = config.getConfigurationSection(playerId);
                if (playerSection != null) {
                    for (String index : playerSection.getKeys(false)) {
                        ConfigurationSection pokemonSection = playerSection.getConfigurationSection(index);
                        if (pokemonSection != null) {
                            String type = pokemonSection.getString("type");
                            String name = pokemonSection.getString("name");
                            long catchTime = pokemonSection.getLong("catchTime");
                            
                            if (type != null && name != null) {
                                pokemonList.add(new CaughtPokemon(type, name, catchTime));
                            }
                        }
                    }
                }
                
                playerData.put(uuid, pokemonList);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("無效的玩家 UUID: " + playerId);
            }
        }
        
        return playerData;
    }
    
    /**
     * 儲存玩家資料
     */
    public void savePlayerData(UUID playerId, List<CaughtPokemon> pokemonList) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerDataFile);
        
        String playerIdStr = playerId.toString();
        ConfigurationSection playerSection = config.createSection(playerIdStr);
        
        for (int i = 0; i < pokemonList.size(); i++) {
            CaughtPokemon pokemon = pokemonList.get(i);
            ConfigurationSection pokemonSection = playerSection.createSection(String.valueOf(i));
            
            pokemonSection.set("type", pokemon.getType());
            pokemonSection.set("name", pokemon.getName());
            pokemonSection.set("catchTime", pokemon.getCatchTime());
        }
        
        try {
            config.save(playerDataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("無法儲存玩家資料: " + e.getMessage());
        }
    }
    
    /**
     * 儲存所有玩家資料
     */
    public void saveAllPlayerData(Map<UUID, List<CaughtPokemon>> playerData) {
        FileConfiguration config = new YamlConfiguration();
        
        for (Map.Entry<UUID, List<CaughtPokemon>> entry : playerData.entrySet()) {
            String playerIdStr = entry.getKey().toString();
            List<CaughtPokemon> pokemonList = entry.getValue();
            
            ConfigurationSection playerSection = config.createSection(playerIdStr);
            
            for (int i = 0; i < pokemonList.size(); i++) {
                CaughtPokemon pokemon = pokemonList.get(i);
                ConfigurationSection pokemonSection = playerSection.createSection(String.valueOf(i));
                
                pokemonSection.set("type", pokemon.getType());
                pokemonSection.set("name", pokemon.getName());
                pokemonSection.set("catchTime", pokemon.getCatchTime());
            }
        }
        
        try {
            config.save(playerDataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("無法儲存所有玩家資料: " + e.getMessage());
        }
    }
    
    /**
     * 載入物資生成點資料
     */
    public Map<String, SupplyPoint> loadSupplyData() {
        Map<String, SupplyPoint> supplyData = new HashMap<>();
        
        if (!supplyDataFile.exists()) {
            return supplyData;
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(supplyDataFile);
        
        for (String key : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(key);
            if (section != null) {
                String worldName = section.getString("world");
                int x = section.getInt("x");
                int y = section.getInt("y");
                int z = section.getInt("z");
                int intervalSeconds = section.getInt("intervalSeconds");
                long createdAt = section.getLong("createdAt");
                
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    Location location = new Location(world, x, y, z);
                    SupplyPoint supplyPoint = new SupplyPoint(location, intervalSeconds);
                    supplyData.put(key, supplyPoint);
                }
            }
        }
        
        return supplyData;
    }
    
    /**
     * 儲存物資生成點資料
     */
    public void saveSupplyData(Map<String, SupplyPoint> supplyData) {
        FileConfiguration config = new YamlConfiguration();
        
        for (Map.Entry<String, SupplyPoint> entry : supplyData.entrySet()) {
            String key = entry.getKey();
            SupplyPoint supplyPoint = entry.getValue();
            Location location = supplyPoint.getLocation();
            
            ConfigurationSection section = config.createSection(key);
            section.set("world", location.getWorld().getName());
            section.set("x", location.getBlockX());
            section.set("y", location.getBlockY());
            section.set("z", location.getBlockZ());
            section.set("intervalSeconds", supplyPoint.getIntervalSeconds());
            section.set("createdAt", supplyPoint.getCreatedAt());
        }
        
        try {
            config.save(supplyDataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("無法儲存物資生成點資料: " + e.getMessage());
        }
    }
    
    /**
     * 根據玩家名稱獲取玩家 UUID
     */
    public UUID getPlayerUUID(String playerName) {
        // 先檢查線上玩家
        Player onlinePlayer = Bukkit.getPlayer(playerName);
        if (onlinePlayer != null) {
            return onlinePlayer.getUniqueId();
        }
        
        // 檢查離線玩家
        try {
            return Bukkit.getOfflinePlayer(playerName).getUniqueId();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 根據 UUID 獲取玩家名稱
     */
    public String getPlayerName(UUID playerId) {
        // 先檢查線上玩家
        Player onlinePlayer = Bukkit.getPlayer(playerId);
        if (onlinePlayer != null) {
            return onlinePlayer.getName();
        }
        
        // 檢查離線玩家
        try {
            return Bukkit.getOfflinePlayer(playerId).getName();
        } catch (Exception e) {
            return "未知玩家";
        }
    }
    
    /**
     * 獲取資料檔案路徑
     */
    public File getDataFolder() {
        return dataFolder;
    }
    
    /**
     * 獲取玩家資料檔案
     */
    public File getPlayerDataFile() {
        return playerDataFile;
    }
    
    /**
     * 獲取物資資料檔案
     */
    public File getSupplyDataFile() {
        return supplyDataFile;
    }
}
