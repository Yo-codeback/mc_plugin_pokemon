package com.icetea520.pokemon.modules;

import com.icetea520.pokemon.PokemonPlugin;
import com.icetea520.pokemon.managers.PokeballManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemGetModule {
    
    private final PokemonPlugin plugin;
    private final PokeballManager pokeballManager;
    
    public ItemGetModule(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.pokeballManager = plugin.getPokeballManager();
    }
    
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c用法: /e get <物品名稱> [數量]");
            player.sendMessage("§e可用物品: pokeball (捕捉球)");
            return;
        }
        
        String itemName = args[1].toLowerCase();
        int amount = 1;
        
        if (args.length >= 3) {
            try {
                amount = Integer.parseInt(args[2]);
                if (amount <= 0 || amount > 64) {
                    player.sendMessage("§c數量必須在 1-64 之間！");
                    return;
                }
            } catch (NumberFormatException e) {
                player.sendMessage("§c無效的數量！");
                return;
            }
        }
        
        switch (itemName) {
            case "pokeball":
            case "捕捉球":
                givePokeball(player, amount);
                break;
            default:
                player.sendMessage("§c未知的物品: " + itemName);
                player.sendMessage("§e可用物品: pokeball (捕捉球)");
                break;
        }
    }
    
    private void givePokeball(Player player, int amount) {
        ItemStack pokeball = pokeballManager.createPokeball(amount);
        player.getInventory().addItem(pokeball);
        player.sendMessage("§a已獲得 " + amount + " 個捕捉球！");
    }
}
