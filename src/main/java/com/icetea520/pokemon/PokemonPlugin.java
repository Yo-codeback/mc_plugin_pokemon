package com.icetea520.pokemon;

import com.icetea520.pokemon.commands.PokemonCommand;
import com.icetea520.pokemon.listeners.*;
import com.icetea520.pokemon.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class PokemonPlugin extends JavaPlugin {
    
    private static PokemonPlugin instance;
    private CatchBagManager catchBagManager;
    private PokeballManager pokeballManager;
    private GameModeManager gameModeManager;
    private GuiBagManager guiBagManager;
    
    @Override
    public void onEnable() {
        try {
            instance = this;
            
            // 載入配置
            saveDefaultConfig();
            reloadConfig();
            
            // 初始化管理器
            this.catchBagManager = new CatchBagManager(this);
            this.pokeballManager = new PokeballManager(this);
            this.gameModeManager = new GameModeManager(this);
            this.guiBagManager = new GuiBagManager(this);
            
            // 註冊指令
            getCommand("e").setExecutor(new PokemonCommand(this));
            getCommand("sc").setExecutor(new PokemonCommand(this));
            
            // 註冊監聽器
            getServer().getPluginManager().registerEvents(new PokeballThrowListener(this), this);
            getServer().getPluginManager().registerEvents(new GuiListener(this), this);
            getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
            
            getLogger().info("§a寶可夢插件已啟用！");
            getLogger().info("§e版本: " + getDescription().getVersion());
            getLogger().info("§e開發者: IceTea_520");
            
        } catch (Exception e) {
            getLogger().severe("§c插件啟用時發生錯誤: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    @Override
    public void onDisable() {
        getLogger().info("§c寶可夢插件已停用！");5
    }
    
    public static PokemonPlugin getInstance() {
        return instance;
    }
    
    public CatchBagManager getCatchBagManager() {
        return catchBagManager;
    }
    
    public PokeballManager getPokeballManager() {
        return pokeballManager;
    }
    
    public GameModeManager getGameModeManager() {
        return gameModeManager;
    }
    
    public GuiBagManager getGuiBagManager() {
        return guiBagManager;
    }
}
