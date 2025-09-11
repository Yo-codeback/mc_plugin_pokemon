package com.icetea520.pokemon.managers;

import com.icetea520.pokemon.PokemonPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SupplyManager {
    
    private final PokemonPlugin plugin;
    private final Map<String, SupplyPoint> supplyPoints; // 物資生成點
    private final Map<String, BukkitTask> supplyTasks; // 定時任務
    
    public SupplyManager(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.supplyPoints = new HashMap<>();
        this.supplyTasks = new HashMap<>();
        
        // 載入物資生成點資料
        loadSupplyData();
    }
    
    /**
     * 創建物資生成點
     */
    public boolean createSupplyPoint(Location location, int intervalSeconds) {
        String key = locationToString(location);
        
        // 檢查是否已存在
        if (supplyPoints.containsKey(key)) {
            return false;
        }
        
        // 創建物資生成點
        SupplyPoint supplyPoint = new SupplyPoint(location, intervalSeconds);
        supplyPoints.put(key, supplyPoint);
        
        // 啟動定時任務
        startSupplyTask(supplyPoint);
        
        // 立即儲存到檔案
        saveSupplyData();
        
        return true;
    }
    
    /**
     * 移除物資生成點
     */
    public boolean removeSupplyPoint(Location location) {
        String key = locationToString(location);
        
        if (!supplyPoints.containsKey(key)) {
            return false;
        }
        
        // 停止定時任務
        stopSupplyTask(key);
        
        // 移除生成點
        supplyPoints.remove(key);
        
        // 立即儲存到檔案
        saveSupplyData();
        
        return true;
    }
    
    /**
     * 更新物資生成點配置
     */
    public boolean updateSupplyPointConfig(Location location, int intervalSeconds) {
        String key = locationToString(location);
        
        if (!supplyPoints.containsKey(key)) {
            return false;
        }
        
        SupplyPoint supplyPoint = supplyPoints.get(key);
        
        // 停止舊任務
        stopSupplyTask(key);
        
        // 更新配置
        supplyPoint.setIntervalSeconds(intervalSeconds);
        
        // 啟動新任務
        startSupplyTask(supplyPoint);
        
        // 立即儲存到檔案
        saveSupplyData();
        
        return true;
    }
    
    /**
     * 啟動物資生成任務
     */
    private void startSupplyTask(SupplyPoint supplyPoint) {
        String key = locationToString(supplyPoint.getLocation());
        
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                generateSupply(supplyPoint);
            }
        }.runTaskTimer(plugin, 0L, supplyPoint.getIntervalSeconds() * 20L); // 轉換為 ticks
        
        supplyTasks.put(key, task);
    }
    
    /**
     * 停止物資生成任務
     */
    private void stopSupplyTask(String key) {
        BukkitTask task = supplyTasks.get(key);
        if (task != null) {
            task.cancel();
            supplyTasks.remove(key);
        }
    }
    
    /**
     * 生成物資
     */
    private void generateSupply(SupplyPoint supplyPoint) {
        Location location = supplyPoint.getLocation();
        World world = location.getWorld();
        
        if (world == null) {
            return;
        }
        
        // 創建捕捉球
        ItemStack pokeball = plugin.getPokeballManager().createPokeball(1);
        
        // 在指定位置掉落物品
        Item droppedItem = world.dropItem(location, pokeball);
        if (droppedItem != null) {
            // 設置物品不會消失
            droppedItem.setUnlimitedLifetime(true);
            droppedItem.setCustomName("§6§l物資補給");
            droppedItem.setCustomNameVisible(true);
        }
        
        // 發送訊息給附近玩家
        world.getPlayers().stream()
            .filter(player -> player.getLocation().distance(location) <= 50)
            .forEach(player -> {
                player.sendMessage("§6§l[物資補給] §e在 " + 
                    location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + 
                    " 生成了物資！");
            });
    }
    
    /**
     * 獲取所有物資生成點
     */
    public Map<String, SupplyPoint> getAllSupplyPoints() {
        return new HashMap<>(supplyPoints);
    }
    
    /**
     * 獲取指定位置的物資生成點
     */
    public SupplyPoint getSupplyPoint(Location location) {
        return supplyPoints.get(locationToString(location));
    }
    
    /**
     * 檢查位置是否有物資生成點
     */
    public boolean hasSupplyPoint(Location location) {
        return supplyPoints.containsKey(locationToString(location));
    }
    
    /**
     * 將位置轉換為字串鍵值
     */
    private String locationToString(Location location) {
        return location.getWorld().getName() + ":" + 
               location.getBlockX() + ":" + 
               location.getBlockY() + ":" + 
               location.getBlockZ();
    }
    
    /**
     * 從字串鍵值解析位置
     */
    public Location stringToLocation(String key) {
        String[] parts = key.split(":");
        if (parts.length != 4) {
            return null;
        }
        
        World world = Bukkit.getWorld(parts[0]);
        if (world == null) {
            return null;
        }
        
        try {
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int z = Integer.parseInt(parts[3]);
            
            return new Location(world, x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * 停止所有物資生成任務
     */
    public void stopAllTasks() {
        for (BukkitTask task : supplyTasks.values()) {
            task.cancel();
        }
        supplyTasks.clear();
    }
    
    /**
     * 載入物資生成點資料
     */
    private void loadSupplyData() {
        if (plugin.getDataManager() != null) {
            Map<String, SupplyPoint> loadedData = plugin.getDataManager().loadSupplyData();
            supplyPoints.putAll(loadedData);
            
            // 重新啟動所有定時任務
            for (SupplyPoint supplyPoint : supplyPoints.values()) {
                startSupplyTask(supplyPoint);
            }
            
            plugin.getLogger().info("已載入 " + loadedData.size() + " 個物資生成點");
        }
    }
    
    /**
     * 儲存物資生成點資料
     */
    private void saveSupplyData() {
        if (plugin.getDataManager() != null) {
            plugin.getDataManager().saveSupplyData(supplyPoints);
        }
    }
    
    /**
     * 物資生成點類別
     */
    public static class SupplyPoint {
        private final Location location;
        private int intervalSeconds;
        private final long createdAt;
        
        public SupplyPoint(Location location, int intervalSeconds) {
            this.location = location.clone();
            this.intervalSeconds = intervalSeconds;
            this.createdAt = System.currentTimeMillis();
        }
        
        public Location getLocation() {
            return location;
        }
        
        public int getIntervalSeconds() {
            return intervalSeconds;
        }
        
        public void setIntervalSeconds(int intervalSeconds) {
            this.intervalSeconds = intervalSeconds;
        }
        
        public long getCreatedAt() {
            return createdAt;
        }
        
        @Override
        public String toString() {
            return "SupplyPoint{" +
                    "location=" + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() +
                    ", interval=" + intervalSeconds + "s" +
                    ", world=" + location.getWorld().getName() +
                    '}';
        }
    }
}
