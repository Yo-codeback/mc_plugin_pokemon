package com.icetea520.pokemon.modules;

import com.icetea520.pokemon.PokemonPlugin;
import com.icetea520.pokemon.managers.LevelManager;
import org.bukkit.entity.Player;

public class LevelModule {
    
    private final PokemonPlugin plugin;
    private final LevelManager levelManager;
    
    public LevelModule(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.levelManager = plugin.getLevelManager();
    }
    
    public void execute(Player player, String[] args) {
        // 更新玩家等級
        levelManager.updatePlayerLevel(player);
        
        // 獲取玩家資訊
        int currentLevel = levelManager.getPlayerLevel(player);
        int caughtCount = plugin.getCatchBagManager().getBagSize(player);
        int catchCountToNext = levelManager.getCatchCountToNextLevel(player);
        double progress = levelManager.getLevelProgress(player);
        
        // 顯示等級資訊
        displayLevelInfo(player, currentLevel, caughtCount, catchCountToNext, progress);
    }
    
    /**
     * 顯示等級資訊
     */
    private void displayLevelInfo(Player player, int currentLevel, int caughtCount, 
                                 int catchCountToNext, double progress) {
        
        player.sendMessage("§6§l=== 寶可夢訓練師等級 ===");
        player.sendMessage("§e當前等級: §a" + currentLevel + "§e/100");
        player.sendMessage("§e捕捉數量: §a" + caughtCount + "§e 隻");
        
        if (currentLevel >= 100) {
            player.sendMessage("§6§l恭喜！您已達到最高等級！");
        } else {
            player.sendMessage("§e距離下一等級: §a" + catchCountToNext + "§e 隻");
            player.sendMessage("§e進度: §a" + String.format("%.1f", progress) + "%");
            
            // 顯示進度條
            String progressBar = createProgressBar(progress);
            player.sendMessage("§7[" + progressBar + "§7]");
        }
        
        // 顯示等級獎勵資訊
        displayLevelRewards(player, currentLevel);
        
        player.sendMessage("§6§l========================");
    }
    
    /**
     * 創建進度條
     */
    private String createProgressBar(double progress) {
        int barLength = 20;
        int filledLength = (int) (progress / 100.0 * barLength);
        
        StringBuilder bar = new StringBuilder();
        bar.append("§a");
        
        for (int i = 0; i < filledLength; i++) {
            bar.append("█");
        }
        
        bar.append("§7");
        for (int i = filledLength; i < barLength; i++) {
            bar.append("█");
        }
        
        return bar.toString();
    }
    
    /**
     * 顯示等級獎勵資訊
     */
    private void displayLevelRewards(Player player, int currentLevel) {
        if (currentLevel >= 10) {
            player.sendMessage("§6§l等級獎勵:");
        }
        
        if (currentLevel >= 10) {
            player.sendMessage("§e等級 10: §a解鎖高級捕捉球");
        }
        if (currentLevel >= 25) {
            player.sendMessage("§e等級 25: §a解鎖超級捕捉球");
        }
        if (currentLevel >= 50) {
            player.sendMessage("§e等級 50: §a解鎖大師捕捉球");
        }
        if (currentLevel >= 75) {
            player.sendMessage("§e等級 75: §a解鎖傳說捕捉球");
        }
        if (currentLevel >= 100) {
            player.sendMessage("§e等級 100: §6§l解鎖所有功能！");
        }
    }
}
