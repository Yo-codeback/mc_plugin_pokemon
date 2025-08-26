package com.icetea520.pokemon.modules;

import com.icetea520.pokemon.PokemonPlugin;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class ModeSwitchModule {
    
    private final PokemonPlugin plugin;
    
    public ModeSwitchModule(PokemonPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void execute(Player player, String[] args) {
        GameMode currentMode = player.getGameMode();
        
        if (currentMode == GameMode.SURVIVAL) {
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage("§a已切換到冒險模式！");
        } else if (currentMode == GameMode.ADVENTURE) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage("§a已切換到生存模式！");
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage("§a已切換到生存模式！");
        }
    }
}
