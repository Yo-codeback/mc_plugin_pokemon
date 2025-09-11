package com.icetea520.pokemon.managers;

import com.icetea520.pokemon.PokemonPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LevelManager {
    
    private final PokemonPlugin plugin;
    private final Map<UUID, Integer> playerLevels; // 玩家等級快取
    private final int[] levelRequirements; // 等級需求表
    
    public LevelManager(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.playerLevels = new HashMap<>();
        this.levelRequirements = initializeLevelRequirements();
    }
    
    /**
     * 初始化等級需求表
     * 根據提供的等級表數據
     */
    private int[] initializeLevelRequirements() {
        return new int[]{
            0,      // 等級 1
            50,     // 等級 2
            100,    // 等級 3
            150,    // 等級 4
            200,    // 等級 5
            250,    // 等級 6
            300,    // 等級 7
            350,    // 等級 8
            400,    // 等級 9
            450,    // 等級 10
            500,    // 等級 11
            600,    // 等級 12
            700,    // 等級 13
            800,    // 等級 14
            900,    // 等級 15
            1000,   // 等級 16
            1100,   // 等級 17
            1200,   // 等級 18
            1300,   // 等級 19
            1400,   // 等級 20
            1500,   // 等級 21
            1650,   // 等級 22
            1800,   // 等級 23
            1950,   // 等級 24
            2100,   // 等級 25
            2250,   // 等級 26
            2400,   // 等級 27
            2550,   // 等級 28
            2700,   // 等級 29
            2850,   // 等級 30
            3000,   // 等級 31
            3200,   // 等級 32
            3400,   // 等級 33
            3600,   // 等級 34
            3800,   // 等級 35
            4000,   // 等級 36
            4200,   // 等級 37
            4400,   // 等級 38
            4600,   // 等級 39
            4800,   // 等級 40
            5000,   // 等級 41
            5500,   // 等級 42
            6000,   // 等級 43
            6500,   // 等級 44
            7000,   // 等級 45
            7500,   // 等級 46
            8000,   // 等級 47
            8500,   // 等級 48
            9000,   // 等級 49
            9500,   // 等級 50
            10000,  // 等級 51
            11000,  // 等級 52
            12000,  // 等級 53
            13000,  // 等級 54
            14000,  // 等級 55
            15000,  // 等級 56
            16000,  // 等級 57
            17000,  // 等級 58
            18000,  // 等級 59
            19000,  // 等級 60
            20000,  // 等級 61
            22000,  // 等級 62
            24000,  // 等級 63
            26000,  // 等級 64
            28000,  // 等級 65
            30000,  // 等級 66
            32000,  // 等級 67
            34000,  // 等級 68
            36000,  // 等級 69
            38000,  // 等級 70
            40000,  // 等級 71
            45000,  // 等級 72
            50000,  // 等級 73
            55000,  // 等級 74
            60000,  // 等級 75
            65000,  // 等級 76
            70000,  // 等級 77
            75000,  // 等級 78
            80000,  // 等級 79
            85000,  // 等級 80
            90000,  // 等級 81
            100000, // 等級 82
            110000, // 等級 83
            120000, // 等級 84
            130000, // 等級 85
            140000, // 等級 86
            150000, // 等級 87
            160000, // 等級 88
            170000, // 等級 89
            180000, // 等級 90
            190000, // 等級 91
            200000, // 等級 92
            210000, // 等級 93
            220000, // 等級 94
            230000, // 等級 95
            240000, // 等級 96
            250000, // 等級 97
            260000, // 等級 98
            270000, // 等級 99
            280000  // 等級 100
        };
    }
    
    /**
     * 根據捕捉數量計算玩家等級
     */
    public int calculateLevel(int caughtCount) {
        for (int i = levelRequirements.length - 1; i >= 0; i--) {
            if (caughtCount >= levelRequirements[i]) {
                return i + 1; // 等級從1開始
            }
        }
        return 1; // 最低等級
    }
    
    /**
     * 獲取玩家當前等級
     */
    public int getPlayerLevel(Player player) {
        int caughtCount = plugin.getCatchBagManager().getBagSize(player);
        return calculateLevel(caughtCount);
    }
    
    /**
     * 獲取玩家當前等級（快取版本）
     */
    public int getPlayerLevelCached(Player player) {
        UUID playerId = player.getUniqueId();
        if (playerLevels.containsKey(playerId)) {
            return playerLevels.get(playerId);
        }
        
        int level = getPlayerLevel(player);
        playerLevels.put(playerId, level);
        return level;
    }
    
    /**
     * 更新玩家等級快取
     */
    public void updatePlayerLevel(Player player) {
        UUID playerId = player.getUniqueId();
        int newLevel = getPlayerLevel(player);
        int oldLevel = playerLevels.getOrDefault(playerId, 1);
        
        playerLevels.put(playerId, newLevel);
        
        // 檢查是否升級
        if (newLevel > oldLevel) {
            onPlayerLevelUp(player, oldLevel, newLevel);
        }
    }
    
    /**
     * 玩家升級事件處理
     */
    private void onPlayerLevelUp(Player player, int oldLevel, int newLevel) {
        player.sendMessage("§6§l恭喜！您升級了！");
        player.sendMessage("§e等級: " + oldLevel + " → " + newLevel);
        player.sendMessage("§a當前捕捉數量: " + plugin.getCatchBagManager().getBagSize(player));
        
        // 發送升級特效
        player.sendTitle("§6§l升級！", "§e等級 " + newLevel, 10, 40, 10);
    }
    
    /**
     * 獲取升級到下一個等級需要的捕捉數量
     */
    public int getCatchCountToNextLevel(Player player) {
        int currentLevel = getPlayerLevel(player);
        int currentCatchCount = plugin.getCatchBagManager().getBagSize(player);
        
        if (currentLevel >= 100) {
            return 0; // 已達最高等級
        }
        
        int nextLevelRequirement = levelRequirements[currentLevel]; // 下一個等級的需求
        return nextLevelRequirement - currentCatchCount;
    }
    
    /**
     * 獲取等級進度百分比
     */
    public double getLevelProgress(Player player) {
        int currentLevel = getPlayerLevel(player);
        int currentCatchCount = plugin.getCatchBagManager().getBagSize(player);
        
        if (currentLevel >= 100) {
            return 100.0; // 已達最高等級
        }
        
        int currentLevelRequirement = levelRequirements[currentLevel - 1]; // 當前等級需求
        int nextLevelRequirement = levelRequirements[currentLevel]; // 下一個等級需求
        
        int progress = currentCatchCount - currentLevelRequirement;
        int totalNeeded = nextLevelRequirement - currentLevelRequirement;
        
        return (double) progress / totalNeeded * 100.0;
    }
    
    /**
     * 獲取等級需求表
     */
    public int[] getLevelRequirements() {
        return levelRequirements.clone();
    }
    
    /**
     * 獲取最大等級
     */
    public int getMaxLevel() {
        return levelRequirements.length;
    }
    
    /**
     * 清理玩家資料
     */
    public void onPlayerQuit(Player player) {
        playerLevels.remove(player.getUniqueId());
    }
}
