package me.mattyhd0.ChatColor;

import me.mattyhd0.ChatColor.Commands.ChatColorAdminCommand;
import me.mattyhd0.ChatColor.GUI.GuiListener;
import me.mattyhd0.ChatColor.PatternAPI.PatternLoader;
import me.mattyhd0.ChatColor.Utility.UpdateChecker;
import me.mattyhd0.ChatColor.Utility.Util;
import me.mattyhd0.ChatColor.bStats.Metrics;
import org.bukkit.command.ConsoleCommandSender;
import me.mattyhd0.ChatColor.Configuration.Config;
import me.mattyhd0.ChatColor.PlaceholderAPI.ChatColorPlaceholders;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import me.mattyhd0.ChatColor.Listeners.StaffJoinListener;
import me.mattyhd0.ChatColor.Listeners.ChatListener;
import me.mattyhd0.ChatColor.Commands.ChatColorCommand;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatColor extends JavaPlugin {

    private static List<String> supportedPlugins = new ArrayList<>();
    private static Plugin plugin;
    private static String prefix;
    
    public void onEnable() {
        setPlugin(this);
        prefix = Util.color("&8[&4&lC&c&lh&6&la&e&lt&2&lC&a&lo&b&ll&3&lo&1&lr&8]");
        Bukkit.getConsoleSender().sendMessage(Util.color(prefix+" &7Enabling ChatColor v" + this.getDescription().getVersion()));
        Metrics metrics = new Metrics(this, 11648);
        //saySupport("Vault");
        saySupport("PlaceholderAPI");
        saySupport("AdvancedColorAPI");
        Config.loadConfiguration();
        setupConfig();
        setupListeners();
        setupCommands();
        updateChecker();
        setupPlaceholderAPI();
        PatternLoader.loadAllPatterns();
    }
    
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Util.color(prefix+" &7Disabling ChatColor v" + this.getDescription().getVersion()));
    }
    
    public void setupConfig() {
        final File config = new File(this.getDataFolder(), "config.yml");
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
        }
    }

    public void setupListeners(){
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new StaffJoinListener(), this);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
    }

    public void setupCommands(){
        getCommand("chatcolor").setExecutor(new ChatColorCommand(this));
        getCommand("chatcolor").setTabCompleter(new ChatColorCommand(this));
        getCommand("chatcoloradmin").setExecutor(new ChatColorAdminCommand(this));
        getCommand("chatcoloradmin").setTabCompleter(new ChatColorAdminCommand(this));
    }

    private static void setPlugin(Plugin pl) {
        plugin = pl;
    }
    
    public static Plugin get() {
        return plugin;
    }
    
    public void setupPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ChatColorPlaceholders().register();
        }
    }
    
    private void updateChecker() {
        if (Config.getBoolean("config.update-checker")) {
            UpdateChecker updateChecker = new UpdateChecker();
            ConsoleCommandSender console = Bukkit.getConsoleSender();
            if (updateChecker.taskIsValid()) {
                if (updateChecker.isRunningLatestVersion()) {
                    String message = Util.color(prefix+" &7You are using the latest version of ChatColor!");
                    console.sendMessage(message);
                } else {
                    String message = Util.color(prefix+" &7You are using version &a" + updateChecker.getVersion() + "&7 and the latest version is &a" + updateChecker.getLatestVersion());
                    String message2 = Util.color(prefix+" &7You can download the latest version at: &a" + updateChecker.getSpigotUrl());
                    console.sendMessage(message);
                    console.sendMessage(message2);
                }
            } else {
                String message = Util.color(prefix+" &7Could not verify if you are using the latest version of ChatColor :(");
                String message2 = Util.color(prefix+" &7You can disable update checker in config.yml file");
                console.sendMessage(message);
                console.sendMessage(message2);
            }
        }
    }

    public void saySupport(String plugin){

        boolean support = Bukkit.getPluginManager().getPlugin(plugin) != null;
        String supportStr = "&cNo";

        if(support) {
            supportStr = "&aYes";
            supportedPlugins.add(plugin);
        }

        Bukkit.getConsoleSender().sendMessage(Util.color( prefix+"&7 "+plugin+" support: "+supportStr));

    }

    public static boolean supportPlugin(String plugin){
        return supportedPlugins.contains(plugin);
    }
}
