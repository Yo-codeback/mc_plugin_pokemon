package com.icetea520.pokemon.modules;

import com.icetea520.pokemon.PokemonPlugin;
import com.icetea520.pokemon.managers.GameModeManager;
import org.bukkit.entity.Player;

public class PokeballModule {
    
    private final PokemonPlugin plugin;
    private final GameModeManager gameModeManager;
    
    public PokeballModule(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.gameModeManager = plugin.getGameModeManager();
    }
    
    public void execute(Player player, String[] args) {
        // 檢查是否在抓寶模式
        if (!gameModeManager.isInPokemonMode(player)) {
            player.sendMessage("§c您必須在抓寶模式下才能使用捕捉球！");
            player.sendMessage("§e使用 /e mode 切換到抓寶模式");
            return;
        }
        
        player.sendMessage("§e請手持捕捉球，然後右鍵丟出！");
        player.sendMessage("§7成功機率: 25%");
        player.sendMessage("§7沒丟中會自動返回");
        player.sendMessage("§7提示: 使用 /e get pokeball 來獲取捕捉球");
    }
}
