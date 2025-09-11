package com.icetea520.pokemon.listeners;

import com.icetea520.pokemon.PokemonPlugin;
import com.icetea520.pokemon.managers.GameModeManager;
import com.icetea520.pokemon.managers.GuiBagManager;
import com.icetea520.pokemon.managers.LevelManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    
    private final PokemonPlugin plugin;
    private final GameModeManager gameModeManager;
    private final GuiBagManager guiBagManager;
    private final LevelManager levelManager;
    
    public PlayerQuitListener(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.gameModeManager = plugin.getGameModeManager();
        this.guiBagManager = plugin.getGuiBagManager();
        this.levelManager = plugin.getLevelManager();
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        // 清理遊戲模式資料
        gameModeManager.onPlayerQuit(player);
        
        // 清理 GUI 資料
        guiBagManager.onInventoryClose(player);
        
        // 清理等級資料
        levelManager.onPlayerQuit(player);
    }
}
