package com.icetea520.pokemon.modules;

import com.icetea520.pokemon.PokemonPlugin;
import org.bukkit.entity.Player;

public class PokeballModule {
    
    private final PokemonPlugin plugin;
    
    public PokeballModule(PokemonPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void execute(Player player, String[] args) {
        player.sendMessage("§e請手持捕捉球，然後右鍵點擊生物來捕捉！");
        player.sendMessage("§7提示: 使用 /e get pokeball 來獲取捕捉球");
    }
}
