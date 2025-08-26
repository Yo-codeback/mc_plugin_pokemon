package com.icetea520.pokemon.modules;

import com.icetea520.pokemon.PokemonPlugin;
import com.icetea520.pokemon.managers.CatchBagManager;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CatchBagModule {
    
    private final PokemonPlugin plugin;
    private final CatchBagManager catchBagManager;
    
    public CatchBagModule(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.catchBagManager = plugin.getCatchBagManager();
    }
    
    public void execute(Player player, String[] args) {
        List<CatchBagManager.CaughtPokemon> bag = catchBagManager.getPlayerBag(player);
        int bagSize = catchBagManager.getBagSize(player);
        
        player.sendMessage("§6=== 捕捉背包 ===");
        player.sendMessage("§e容量: " + bagSize + "/100");
        
        if (bag.isEmpty()) {
            player.sendMessage("§7背包是空的，快去捕捉寶可夢吧！");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            for (int i = 0; i < Math.min(bag.size(), 10); i++) {
                CatchBagManager.CaughtPokemon pokemon = bag.get(i);
                String catchTime = sdf.format(new Date(pokemon.getCatchTime()));
                
                player.sendMessage(String.format("§a%d. %s §7(%s) §8- %s", 
                    i + 1, 
                    pokemon.getName(), 
                    pokemon.getType(), 
                    catchTime
                ));
            }
            
            if (bag.size() > 10) {
                player.sendMessage("§7... 還有 " + (bag.size() - 10) + " 個寶可夢");
            }
        }
        
        player.sendMessage("§6================");
    }
}
