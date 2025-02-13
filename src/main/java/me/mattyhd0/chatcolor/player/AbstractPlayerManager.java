package me.mattyhd0.chatcolor.player;

import me.mattyhd0.chatcolor.ChatColorPlugin;
import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public abstract class AbstractPlayerManager implements Listener {

    @NotNull
    private final Map<UUID, CPlayer> onlinePlayers;
    @NotNull
    protected final ChatColorPlugin plugin;
    @NotNull
    protected final ExecutorService executorService;

    protected AbstractPlayerManager(final @NotNull ChatColorPlugin plugin) {
        this.plugin = plugin;
        this.onlinePlayers = new ConcurrentHashMap<>();
        this.executorService = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("ChatColor-%d").factory());
    }

    public @NotNull Map<UUID, CPlayer> getOnlinePlayers() {
        return onlinePlayers;
    }

    protected abstract CompletionStage<Boolean> savePlayer(final @NotNull UUID uuid, final @Nullable BasePattern pattern);

    protected abstract CompletionStage<CPlayer> loadPlayer(final @NotNull UUID uuid);

    @EventHandler
    public void onAsyncLogin(final @NotNull AsyncPlayerPreLoginEvent event){
        final var uuid = event.getUniqueId();
        if(this.onlinePlayers.containsKey(uuid)) return;

        this.loadPlayer(uuid).whenComplete((cPlayer, throwable) -> {
            if(throwable != null){
                plugin.getLogger().log(Level.SEVERE, "An error occurred while loading player data for " + uuid, throwable);
                return;
            }
            this.onlinePlayers.put(uuid, cPlayer);
        });
    }

    @EventHandler
    public void onQuit(final @NotNull PlayerQuitEvent event){
        final var uuid = event.getPlayer().getUniqueId();
        if(!this.onlinePlayers.containsKey(uuid)) return;

        final var cPlayer = this.onlinePlayers.get(uuid);
        final var basePattern = cPlayer.getPattern();

        this.savePlayer(uuid, basePattern).whenComplete((result, throwable) -> {
            if(throwable != null || !result){
                plugin.getLogger().log(Level.SEVERE, "An error occurred while saving player data for " + uuid, throwable);
            }
            this.onlinePlayers.remove(uuid);
        });
    }

}
