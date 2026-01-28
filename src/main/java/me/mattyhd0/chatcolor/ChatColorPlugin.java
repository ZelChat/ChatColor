package me.mattyhd0.chatcolor;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.mattyhd0.chatcolor.command.ChatColorAdminCommand;
import me.mattyhd0.chatcolor.command.ChatColorCommand;
import me.mattyhd0.chatcolor.configuration.ConfigurationManager;
import me.mattyhd0.chatcolor.configuration.SimpleYMLConfiguration;
import me.mattyhd0.chatcolor.gui.GuiListener;
import me.mattyhd0.chatcolor.pattern.manager.PatternManager;
import me.mattyhd0.chatcolor.placeholderapi.ChatColorPlaceholders;
import me.mattyhd0.chatcolor.player.AbstractPlayerManager;
import me.mattyhd0.chatcolor.player.CPlayer;
import me.mattyhd0.chatcolor.player.impl.SQLPlayerManager;
import me.mattyhd0.chatcolor.player.impl.YamlPlayerManager;
import me.mattyhd0.chatcolor.util.Util;
import me.nahu.scheduler.wrapper.WrappedScheduler;
import me.nahu.scheduler.wrapper.WrappedSchedulerBuilder;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChatColorPlugin extends JavaPlugin {

    private static ChatColorPlugin INSTANCE;
    private PatternManager patternManager;
    private ConfigurationManager configurationManager;
    private AbstractPlayerManager playerManager;
    private final List<String> supportedPlugins = new ArrayList<>();
    private String prefix;
    private HikariDataSource hikariConnectionPool;

    private WrappedScheduler scheduler;

    @Override
    public void onLoad() {
        INSTANCE = this;
        scheduler = WrappedSchedulerBuilder.builder().plugin(this).build();
    }

    @Override
    public void onEnable() {
        prefix = Util.color("&8[&4&lC&c&lh&6&la&e&lt&2&lC&a&lo&b&ll&3&lo&1&lr&8]");
        Bukkit.getConsoleSender().sendMessage(Util.color(prefix+" &7Enabling ChatColor v" + this.getDescription().getVersion()));
        new Metrics(this, 11648);
        saySupport("PlaceholderAPI");
        reload();
        if(configurationManager.getConfig().getBoolean("config.mysql.enable")){
            this.playerManager = new SQLPlayerManager(this);
        }else{
            this.playerManager = new YamlPlayerManager(this);
        }
        setupListeners();
        setupCommands();
        setupPlaceholderAPI();
    }

    public void reload(){
        configurationManager = new ConfigurationManager();
        patternManager = new PatternManager();
        if(hikariConnectionPool != null){
            hikariConnectionPool.close();
            hikariConnectionPool = null;
        }
        if(configurationManager.getConfig().getBoolean("config.mysql.enable")) openMysqlConnection();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Util.color(prefix+" &7Disabling ChatColor v" + this.getDescription().getVersion()));
        if(hikariConnectionPool != null) {
            hikariConnectionPool.close();
        }
    }

    public void setupListeners(){
        getServer().getPluginManager().registerEvents(playerManager, this);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
    }

    public void setupCommands(){
        ChatColorCommand chatColorCommand = new ChatColorCommand(this);
        ChatColorAdminCommand chatColorAdminCommand = new ChatColorAdminCommand(this);
        getCommand("chatcolor").setExecutor(chatColorCommand);
        getCommand("chatcolor").setTabCompleter(chatColorCommand);
        getCommand("chatcoloradmin").setExecutor(chatColorAdminCommand);
        getCommand("chatcoloradmin").setTabCompleter(chatColorAdminCommand);
    }
    
    public void setupPlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ChatColorPlaceholders().register();
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

    public void openMysqlConnection(){

        SimpleYMLConfiguration config = configurationManager.getConfig();

        String host     = config.getString("config.mysql.host");
        String port     = config.getString("config.mysql.port");
        String username = config.getString("config.mysql.username");
        String password = config.getString("config.mysql.password");
        String database = config.getString("config.mysql.database");
        String additionalUrl = config.getString("config.mysql.additional-url","&useSSL=false&&autoReconnect=true");

        try{

            String urlConnection = ("jdbc:mysql://{host}:{port}/{database}?user={username}&password={password}"+additionalUrl)
                    .replaceAll("\\{host}", host)
                    .replaceAll("\\{port}", port)
                    .replaceAll("\\{username}", username)
                    .replaceAll("\\{password}", password)
                    .replaceAll("\\{database}", database);

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(urlConnection);

            this.hikariConnectionPool = new HikariDataSource(hikariConfig);

            Connection connection = this.hikariConnectionPool.getConnection();
            try (Statement statement = connection.createStatement()){
                statement.execute("CREATE TABLE IF NOT EXISTS playerdata ( uuid varchar(36) NOT NULL, pattern varchar(45) NOT NULL, PRIMARY KEY (uuid) );");
            }
            connection.close();

        } catch (SQLException e){
            Bukkit.getServer().getConsoleSender().sendMessage(
                    Util.color("&c[ChatColor] There was an error connecting to the MySQL Database")
            );
            e.printStackTrace();
        }


    }

    public boolean supportPlugin(String plugin){
        return supportedPlugins.contains(plugin);
    }

    public static ChatColorPlugin getInstance() {
        return INSTANCE;
    }

    public HikariDataSource getConnectionPool() {
        return this.hikariConnectionPool;
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public PatternManager getPatternManager() {
        return patternManager;
    }

    public void sendConsoleMessage(String message){
        getServer().getConsoleSender().sendMessage(prefix+" "+Util.color(message));
    }

    public String getPrefix() {
        return prefix;
    }

    public Map<UUID, CPlayer> getDataMap() {
        return playerManager.getOnlinePlayers();
    }

    public WrappedScheduler getScheduler() {
        return scheduler;
    }
}
