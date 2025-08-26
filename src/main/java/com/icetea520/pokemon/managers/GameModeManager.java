package com.icetea520.pokemon.managers;

import com.icetea520.pokemon.PokemonPlugin;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GameModeManager {
    
    private final PokemonPlugin plugin;
    private final Set<UUID> pokemonModePlayers;
    
    public GameModeManager(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.pokemonModePlayers = new HashSet<>();
    }
    
    /**
     * 將玩家切換到抓寶模式
     */
    public void enablePokemonMode(Player player) {
        pokemonModePlayers.add(player.getUniqueId());
        player.setGameMode(GameMode.ADVENTURE);
        player.sendMessage("§a已切換到抓寶模式！");
        player.sendMessage("§e在此模式下可以使用所有寶可夢功能！");
    }
    
    /**
     * 將玩家切換回生存模式
     */
    public void disablePokemonMode(Player player) {
        pokemonModePlayers.remove(player.getUniqueId());
        player.setGameMode(GameMode.SURVIVAL);
        player.sendMessage("§a已切換回生存模式！");
    }
    
    /**
     * 檢查玩家是否在抓寶模式
     */
    public boolean isInPokemonMode(Player player) {
        return pokemonModePlayers.contains(player.getUniqueId());
    }
    
    /**
     * 獲取所有在抓寶模式的玩家
     */
    public Set<UUID> getPokemonModePlayers() {
        return new HashSet<>(pokemonModePlayers);
    }
    
    /**
     * 玩家離線時清理資料
     */
    public void onPlayerQuit(Player player) {
        pokemonModePlayers.remove(player.getUniqueId());
    }
}
