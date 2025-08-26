package com.icetea520.pokemon.managers;

import com.icetea520.pokemon.PokemonPlugin;
import com.icetea520.pokemon.managers.CatchBagManager.CaughtPokemon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GuiBagManager {
    
    private final PokemonPlugin plugin;
    private final CatchBagManager catchBagManager;
    private final Map<UUID, Integer> playerPages; // 記錄每個玩家的當前頁面
    private static final int ITEMS_PER_PAGE = 45; // 每頁顯示的物品數量 (9x5)
    private static final int TOTAL_SLOTS = 54; // 總槽位數 (9x6)
    
    public GuiBagManager(PokemonPlugin plugin) {
        this.plugin = plugin;
        this.catchBagManager = plugin.getCatchBagManager();
        this.playerPages = new HashMap<>();
    }
    
    /**
     * 開啟玩家的捕捉背包 GUI
     */
    public void openBag(Player player) {
        List<CaughtPokemon> bag = catchBagManager.getPlayerBag(player);
        int currentPage = playerPages.getOrDefault(player.getUniqueId(), 0);
        
        // 計算總頁數
        int totalPages = (int) Math.ceil((double) bag.size() / ITEMS_PER_PAGE);
        if (totalPages == 0) totalPages = 1;
        
        // 確保頁面在有效範圍內
        if (currentPage >= totalPages) {
            currentPage = 0;
        }
        
        // 創建 GUI
        Inventory gui = Bukkit.createInventory(null, TOTAL_SLOTS, 
            "§6捕捉背包 §7(" + (currentPage + 1) + "/" + totalPages + ")");
        
        // 填充物品
        fillItems(gui, bag, currentPage);
        
        // 添加導航按鈕
        addNavigationButtons(gui, currentPage, totalPages);
        
        // 開啟 GUI
        player.openInventory(gui);
        playerPages.put(player.getUniqueId(), currentPage);
    }
    
    /**
     * 填充物品到 GUI
     */
    private void fillItems(Inventory gui, List<CaughtPokemon> bag, int page) {
        int startIndex = page * ITEMS_PER_PAGE;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        for (int i = 0; i < ITEMS_PER_PAGE && (startIndex + i) < bag.size(); i++) {
            CaughtPokemon pokemon = bag.get(startIndex + i);
            ItemStack item = createPokemonItem(pokemon, sdf);
            gui.setItem(i, item);
        }
    }
    
    /**
     * 創建寶可夢物品
     */
    private ItemStack createPokemonItem(CaughtPokemon pokemon, SimpleDateFormat sdf) {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName("§a" + pokemon.getName());
        meta.setLore(List.of(
            "§7類型: " + pokemon.getType(),
            "§7捕捉時間: " + sdf.format(new Date(pokemon.getCatchTime())),
            "§e§l寶可夢"
        ));
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * 添加導航按鈕
     */
    private void addNavigationButtons(Inventory gui, int currentPage, int totalPages) {
        // 上一頁按鈕
        if (currentPage > 0) {
            ItemStack prevButton = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prevButton.getItemMeta();
            prevMeta.setDisplayName("§e上一頁");
            prevButton.setItemMeta(prevMeta);
            gui.setItem(45, prevButton);
        }
        
        // 下一頁按鈕
        if (currentPage < totalPages - 1) {
            ItemStack nextButton = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextButton.getItemMeta();
            nextMeta.setDisplayName("§e下一頁");
            nextButton.setItemMeta(nextMeta);
            gui.setItem(53, nextButton);
        }
        
        // 背包資訊
        ItemStack infoItem = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = infoItem.getItemMeta();
        infoMeta.setDisplayName("§6背包資訊");
        infoMeta.setLore(List.of(
            "§7當前頁面: " + (currentPage + 1) + "/" + totalPages,
            "§7總容量: 100格",
            "§e§l寶可夢插件"
        ));
        infoItem.setItemMeta(infoMeta);
        gui.setItem(49, infoItem);
    }
    
    /**
     * 處理 GUI 點擊事件
     */
    public boolean handleClick(Player player, int slot) {
        if (slot == 45) { // 上一頁
            int currentPage = playerPages.getOrDefault(player.getUniqueId(), 0);
            if (currentPage > 0) {
                playerPages.put(player.getUniqueId(), currentPage - 1);
                openBag(player);
            }
            return true;
        } else if (slot == 53) { // 下一頁
            int currentPage = playerPages.getOrDefault(player.getUniqueId(), 0);
            List<CaughtPokemon> bag = catchBagManager.getPlayerBag(player);
            int totalPages = (int) Math.ceil((double) bag.size() / ITEMS_PER_PAGE);
            if (totalPages == 0) totalPages = 1;
            
            if (currentPage < totalPages - 1) {
                playerPages.put(player.getUniqueId(), currentPage + 1);
                openBag(player);
            }
            return true;
        }
        return false; // 允許正常點擊物品
    }
    
    /**
     * 玩家關閉 GUI 時清理資料
     */
    public void onInventoryClose(Player player) {
        playerPages.remove(player.getUniqueId());
    }
}
