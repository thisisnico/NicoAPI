package net.thisisnico;

import co.aikar.commands.PaperCommandManager;
import net.thisisnico.commands.BasicCommand;
import net.thisisnico.events.BasicHandler;
import net.thisisnico.inventory.InventoryEvents;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class API extends JavaPlugin {

    private static API instance;
    private static FileConfiguration config;
    private static PaperCommandManager commands;
    private static InventoryEvents invEvents;

    @Override
    public void onEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("LunarClient-API")) {
            getLogger().severe("*******************************");
            getLogger().severe("Caution! LunarClientApi is not installed or enabled!");
            getLogger().severe("This API will not work without it.");
            getLogger().severe("*******************************");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            getLogger().severe("*******************************");
            getLogger().severe("Caution! ProtocolLib is not installed or enabled!");
            getLogger().severe("This API will not work without it.");
            getLogger().severe("*******************************");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            getLogger().severe("*******************************");
            getLogger().severe("Caution! HolographicDisplays is not installed or enabled!");
            getLogger().severe("This API will not work without it.");
            getLogger().severe("*******************************");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().severe("*******************************");
            getLogger().severe("Caution! PlaceholderAPI is not installed or enabled!");
            getLogger().severe("This API will not work without it.");
            getLogger().severe("*******************************");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        instance = this;
        config = getConfig();
        commands = new PaperCommandManager(this);
        invEvents = new InventoryEvents();

        Bukkit.getPluginManager().registerEvents(new BasicHandler(), this);
        Bukkit.getPluginManager().registerEvents(invEvents, this);

        commands.enableUnstableAPI("brigadier");

        commands.registerCommand(new BasicCommand());

        getLogger().info("Loaded NicoAPI!");
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public static API getInstance() {
        return instance;
    }

    public static PaperCommandManager getCommandManager() {
        return commands;
    }

    public static FileConfiguration getConf() {
        return config;
    }

    public static InventoryEvents getInventoryEvents() { return invEvents; }

}
