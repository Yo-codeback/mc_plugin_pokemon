package com.icetea520.pokemon.modules;

import com.icetea520.pokemon.PokemonPlugin;
import com.icetea520.pokemon.managers.CatchBagManager;
import com.icetea520.pokemon.managers.GameModeManager;
import com.icetea520.pokemon.managers.GuiBagManager;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class CatchBagModule {
    
    private final PokemonPlugin plugin;
    private final GuiBagManager guiBagManager;
    private final GameModeManager gameModeManager;
    
    public CatchBagModule(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.guiBagManager = plugin.getGuiBagManager();
        this.gameModeManager = plugin.getGameModeManager();
    }
    
    public void execute(Player player, String[] args) {
        // 檢查是否為管理員查看其他玩家背包
        if (args.length >= 2 && player.hasPermission("pokemon.admin")) {
            String targetPlayerName = args[1];
            openOtherPlayerBag(player, targetPlayerName);
            return;
        }
        
        // 檢查是否在抓寶模式
        if (!gameModeManager.isInPokemonMode(player)) {
            player.sendMessage("§c您必須在抓寶模式下才能使用此功能！");
            player.sendMessage("§e使用 /e mode 切換到抓寶模式");
            return;
        }
        
        // 開啟 GUI 背包
        guiBagManager.openBag(player);
    }
    
    /**
     * 開啟其他玩家的背包
     */
    private void openOtherPlayerBag(Player admin, String targetPlayerName) {
        // 獲取目標玩家 UUID
        UUID targetUUID = plugin.getDataManager().getPlayerUUID(targetPlayerName);
        if (targetUUID == null) {
            admin.sendMessage("§c找不到玩家: " + targetPlayerName);
            return;
        }
        
        // 獲取目標玩家背包
        List<CatchBagManager.CaughtPokemon> targetBag = plugin.getCatchBagManager().getPlayerBagByUUID(targetUUID);
        String targetPlayerDisplayName = plugin.getDataManager().getPlayerName(targetUUID);
        
        // 開啟管理員查看模式
        guiBagManager.openOtherPlayerBag(admin, targetBag, targetPlayerDisplayName);
    }
}
