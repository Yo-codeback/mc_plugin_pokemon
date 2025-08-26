package com.icetea520.pokemon.managers;

import com.icetea520.pokemon.PokemonPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;

public class PokeballManager {
    
    private final PokemonPlugin plugin;
    
    public PokeballManager(PokemonPlugin plugin) {
        this.plugin = plugin;
    }
    
    public ItemStack createPokeball(int amount) {
        ItemStack pokeball = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta meta = (SkullMeta) pokeball.getItemMeta();
        
        // 設定捕捉球頭顱
        setPokeballTexture(meta);
        
        meta.setDisplayName("§c§l捕捉球");
        meta.setLore(Arrays.asList(
            "§7丟出捕捉球來捕捉生物",
            "§7成功機率: 25%",
            "§7沒丟中會自動返回",
            "§e§l寶可夢插件"
        ));
        
        pokeball.setItemMeta(meta);
        return pokeball;
    }
    
    private void setPokeballTexture(SkullMeta meta) {
        try {
            PlayerProfile profile = plugin.getServer().createPlayerProfile(UUID.randomUUID());
            PlayerTextures textures = profile.getTextures();
            
            // 使用紅色捕捉球的頭顱材質
            URL url = new URL("http://textures.minecraft.net/texture/1b6f1a25b6bc99f3e75c8d5c53721b0fbf11d8cb5c5b0c0c0c0c0c0c0c0c0c0c0");
            textures.setSkin(url);
            
            meta.setOwnerProfile(profile);
        } catch (MalformedURLException e) {
            // 如果設定失敗，使用預設頭顱
            meta.setOwningPlayer(plugin.getServer().getOfflinePlayer("Pokemon"));
        }
    }
    
    public boolean isPokeball(ItemStack item) {
        if (item == null || item.getType() != Material.PLAYER_HEAD) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return false;
        }
        
        return meta.getDisplayName().equals("§c§l捕捉球");
    }
    
    /**
     * 檢查捕捉是否成功 (1/4 機率)
     */
    public boolean isCatchSuccessful() {
        return Math.random() < 0.25; // 25% 成功率
    }
}
