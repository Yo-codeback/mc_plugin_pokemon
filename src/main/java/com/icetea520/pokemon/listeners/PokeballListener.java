package com.icetea520.pokemon.listeners;

import com.icetea520.pokemon.PokemonPlugin;
import com.icetea520.pokemon.managers.CatchBagManager;
import com.icetea520.pokemon.managers.PokeballManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PokeballListener implements Listener {
    
    private final PokemonPlugin plugin;
    private final PokeballManager pokeballManager;
    private final CatchBagManager catchBagManager;
    
    public PokeballListener(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.pokeballManager = plugin.getPokeballManager();
        this.catchBagManager = plugin.getCatchBagManager();
    }
    
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        // 檢查是否使用捕捉球
        if (!pokeballManager.isPokeball(item)) {
            return;
        }
        
        event.setCancelled(true);
        
        // 檢查是否為可捕捉的生物
        if (!isCatchableEntity(entity)) {
            player.sendMessage("§c此生物無法捕捉！");
            return;
        }
        
        // 嘗試捕捉
        if (catchBagManager.addPokemon(player, entity)) {
            // 捕捉成功
            player.sendMessage("§a成功捕捉到 " + entity.getName() + "！");
            
            // 移除一個捕捉球
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }
            
            // 移除被捕捉的生物
            entity.remove();
        } else {
            player.sendMessage("§c捕捉失敗！背包可能已滿。");
        }
    }
    
    private boolean isCatchableEntity(Entity entity) {
        // 這裡可以定義哪些生物可以被捕捉
        // 目前允許所有生物，除了玩家
        return !(entity instanceof Player);
    }
}
