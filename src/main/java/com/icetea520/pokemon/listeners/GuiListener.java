package com.icetea520.pokemon.listeners;

import com.icetea520.pokemon.PokemonPlugin;
import com.icetea520.pokemon.managers.GuiBagManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GuiListener implements Listener {
    
    private final PokemonPlugin plugin;
    private final GuiBagManager guiBagManager;
    
    public GuiListener(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.guiBagManager = plugin.getGuiBagManager();
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        // 檢查是否為捕捉背包 GUI
        if (title.contains("捕捉背包")) {
            event.setCancelled(true); // 防止移動物品
            
            // 處理導航按鈕點擊
            if (guiBagManager.handleClick(player, event.getRawSlot())) {
                // 按鈕被處理，不需要額外操作
            }
        }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getPlayer();
        String title = event.getView().getTitle();
        
        // 檢查是否為捕捉背包 GUI
        if (title.contains("捕捉背包")) {
            guiBagManager.onInventoryClose(player);
        }
    }
}
