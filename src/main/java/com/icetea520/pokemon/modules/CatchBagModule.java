package com.icetea520.pokemon.modules;

import com.icetea520.pokemon.PokemonPlugin;
import com.icetea520.pokemon.managers.GameModeManager;
import com.icetea520.pokemon.managers.GuiBagManager;
import org.bukkit.entity.Player;

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
        // 檢查是否在抓寶模式
        if (!gameModeManager.isInPokemonMode(player)) {
            player.sendMessage("§c您必須在抓寶模式下才能使用此功能！");
            player.sendMessage("§e使用 /e mode 切換到抓寶模式");
            return;
        }
        
        // 開啟 GUI 背包
        guiBagManager.openBag(player);
    }
}
