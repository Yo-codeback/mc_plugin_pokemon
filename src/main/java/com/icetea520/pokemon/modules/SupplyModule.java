package com.icetea520.pokemon.modules;

import com.icetea520.pokemon.PokemonPlugin;
import com.icetea520.pokemon.managers.SupplyManager;
import org.bukkit.entity.Player;

public class SupplyModule {
    
    private final PokemonPlugin plugin;
    private final SupplyManager supplyManager;
    
    public SupplyModule(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.supplyManager = plugin.getSupplyManager();
    }
    
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            sendHelpMessage(player);
            return;
        }
        
        String subCommand = args[1].toLowerCase();
        
        switch (subCommand) {
            case "giveplace":
                handleGivePlace(player, args);
                break;
            case "remove":
                handleRemove(player, args);
                break;
            case "config":
                handleConfig(player, args);
                break;
            case "list":
                handleList(player, args);
                break;
            case "info":
                handleInfo(player, args);
                break;
            default:
                sendHelpMessage(player);
                break;
        }
    }
    
    /**
     * 處理 giveplace 子指令
     */
    private void handleGivePlace(Player player, String[] args) {
        int intervalSeconds = 30; // 預設30秒
        
        // 解析時間間隔
        if (args.length >= 3) {
            String timeStr = args[2];
            intervalSeconds = parseTimeInterval(timeStr);
            
            if (intervalSeconds <= 0) {
                player.sendMessage("§c無效的時間間隔！請使用格式如: 30s, 1m, 2m30s");
                return;
            }
        }
        
        // 創建物資生成點
        if (supplyManager.createSupplyPoint(player.getLocation(), intervalSeconds)) {
            player.sendMessage("§a成功創建物資生成點！");
            player.sendMessage("§e位置: " + formatLocation(player.getLocation()));
            player.sendMessage("§e生成間隔: " + intervalSeconds + " 秒");
            player.sendMessage("§7使用 §e/e supply remove §7來移除此生成點");
        } else {
            player.sendMessage("§c此位置已存在物資生成點！");
            player.sendMessage("§7使用 §e/e supply config " + intervalSeconds + "s §7來更新配置");
        }
    }
    
    /**
     * 處理 remove 子指令
     */
    private void handleRemove(Player player, String[] args) {
        if (supplyManager.removeSupplyPoint(player.getLocation())) {
            player.sendMessage("§a成功移除物資生成點！");
            player.sendMessage("§e位置: " + formatLocation(player.getLocation()));
        } else {
            player.sendMessage("§c此位置沒有物資生成點！");
        }
    }
    
    /**
     * 處理 config 子指令
     */
    private void handleConfig(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage("§c用法: /e supply config <時間間隔>");
            player.sendMessage("§e範例: /e supply config 40s");
            return;
        }
        
        String timeStr = args[2];
        int intervalSeconds = parseTimeInterval(timeStr);
        
        if (intervalSeconds <= 0) {
            player.sendMessage("§c無效的時間間隔！請使用格式如: 30s, 1m, 2m30s");
            return;
        }
        
        if (supplyManager.updateSupplyPointConfig(player.getLocation(), intervalSeconds)) {
            player.sendMessage("§a成功更新物資生成點配置！");
            player.sendMessage("§e位置: " + formatLocation(player.getLocation()));
            player.sendMessage("§e新生成間隔: " + intervalSeconds + " 秒");
        } else {
            player.sendMessage("§c此位置沒有物資生成點！");
            player.sendMessage("§7使用 §e/e supply giveplace " + intervalSeconds + "s §7來創建");
        }
    }
    
    /**
     * 處理 list 子指令
     */
    private void handleList(Player player, String[] args) {
        var supplyPoints = supplyManager.getAllSupplyPoints();
        
        if (supplyPoints.isEmpty()) {
            player.sendMessage("§e目前沒有物資生成點");
            return;
        }
        
        player.sendMessage("§6§l=== 物資生成點列表 ===");
        int index = 1;
        for (var entry : supplyPoints.entrySet()) {
            SupplyManager.SupplyPoint point = entry.getValue();
            player.sendMessage("§e" + index + ". §a" + formatLocation(point.getLocation()) + 
                             " §7- 間隔: §e" + point.getIntervalSeconds() + "s");
            index++;
        }
        player.sendMessage("§6§l=====================");
    }
    
    /**
     * 處理 info 子指令
     */
    private void handleInfo(Player player, String[] args) {
        SupplyManager.SupplyPoint point = supplyManager.getSupplyPoint(player.getLocation());
        
        if (point == null) {
            player.sendMessage("§c此位置沒有物資生成點！");
            return;
        }
        
        player.sendMessage("§6§l=== 物資生成點資訊 ===");
        player.sendMessage("§e位置: §a" + formatLocation(point.getLocation()));
        player.sendMessage("§e生成間隔: §a" + point.getIntervalSeconds() + " 秒");
        player.sendMessage("§e創建時間: §a" + formatTime(point.getCreatedAt()));
        player.sendMessage("§6§l=====================");
    }
    
    /**
     * 解析時間間隔字串
     */
    private int parseTimeInterval(String timeStr) {
        timeStr = timeStr.toLowerCase().trim();
        
        try {
            if (timeStr.endsWith("s")) {
                // 秒
                return Integer.parseInt(timeStr.substring(0, timeStr.length() - 1));
            } else if (timeStr.endsWith("m")) {
                // 分鐘
                return Integer.parseInt(timeStr.substring(0, timeStr.length() - 1)) * 60;
            } else if (timeStr.contains("m") && timeStr.contains("s")) {
                // 分鐘和秒
                String[] parts = timeStr.split("m");
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1].replace("s", ""));
                return minutes * 60 + seconds;
            } else {
                // 純數字，預設為秒
                return Integer.parseInt(timeStr);
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * 格式化位置字串
     */
    private String formatLocation(org.bukkit.Location location) {
        return location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + 
               " (" + location.getWorld().getName() + ")";
    }
    
    /**
     * 格式化時間
     */
    private String formatTime(long timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date(timestamp));
    }
    
    /**
     * 發送幫助訊息
     */
    private void sendHelpMessage(Player player) {
        player.sendMessage("§6§l=== 物資補給指令 ===");
        player.sendMessage("§e/e supply giveplace [時間間隔] §7- 在當前位置創建物資生成點");
        player.sendMessage("§e/e supply remove §7- 移除當前位置的物資生成點");
        player.sendMessage("§e/e supply config <時間間隔> §7- 更新當前位置的生成間隔");
        player.sendMessage("§e/e supply list §7- 列出所有物資生成點");
        player.sendMessage("§e/e supply info §7- 查看當前位置的生成點資訊");
        player.sendMessage("§7時間格式: 30s, 1m, 2m30s");
        player.sendMessage("§6§l=====================");
    }
}
