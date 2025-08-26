package com.icetea520.pokemon.modules;

import com.icetea520.pokemon.PokemonPlugin;
import com.icetea520.pokemon.managers.GameModeManager;
import org.bukkit.entity.Player;

public class ModeSwitchModule {
    
    private final PokemonPlugin plugin;
    private final GameModeManager gameModeManager;
    
    public ModeSwitchModule(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.gameModeManager = plugin.getGameModeManager();
    }
    
    public void execute(Player player, String[] args) {
        if (gameModeManager.isInPokemonMode(player)) {
            // 如果已在抓寶模式，切換回生存模式
            gameModeManager.disablePokemonMode(player);
        } else {
            // 切換到抓寶模式
            gameModeManager.enablePokemonMode(player);
        }
    }
}
