package com.icetea520.pokemon.managers;

import com.icetea520.pokemon.PokemonPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PokeballManager {
    
    private final PokemonPlugin plugin;
    
    public PokeballManager(PokemonPlugin plugin) {
        this.plugin = plugin;
    }
    
    public ItemStack createPokeball(int amount) {
        ItemStack pokeball = new ItemStack(Material.EGG, amount);
        ItemMeta meta = pokeball.getItemMeta();
        
        meta.setDisplayName("§c§l捕捉球");
        meta.setLore(Arrays.asList(
            "§7右鍵點擊生物來捕捉",
            "§7捕捉成功後會存入背包",
            "§e§l寶可夢插件"
        ));
        
        pokeball.setItemMeta(meta);
        return pokeball;
    }
    
    public boolean isPokeball(ItemStack item) {
        if (item == null || item.getType() != Material.EGG) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return false;
        }
        
        return meta.getDisplayName().equals("§c§l捕捉球");
    }
}
