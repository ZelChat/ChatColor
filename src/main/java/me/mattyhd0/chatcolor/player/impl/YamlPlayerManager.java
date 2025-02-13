package me.mattyhd0.chatcolor.player.impl;

import me.mattyhd0.chatcolor.ChatColorPlugin;
import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import me.mattyhd0.chatcolor.player.AbstractPlayerManager;
import me.mattyhd0.chatcolor.player.CPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public final class YamlPlayerManager extends AbstractPlayerManager {


    public YamlPlayerManager(final @NotNull ChatColorPlugin plugin) {
        super(plugin);
    }

    @Override
    protected CompletionStage<Boolean> savePlayer(final @NotNull UUID uuid, final @Nullable BasePattern pattern) {
        return CompletableFuture.supplyAsync(() -> {
            final var dataFile = plugin.getConfigurationManager().getData();
            final var patternName = pattern == null ? null : pattern.getName(false);
            dataFile.set("data." + uuid, patternName);
            try {
                dataFile.save();
                return true;
            } catch (IOException e) {
                return false;
            }
        }, executorService);
    }

    @Override
    protected CompletionStage<CPlayer> loadPlayer(final @NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            final var dataFile = plugin.getConfigurationManager().getData();
            final var patternName = dataFile.getString("data." + uuid);
            final var basePattern = plugin.getPatternManager().getPatternByName(patternName);
            return new CPlayer(basePattern);
        }, executorService);
    }
}
