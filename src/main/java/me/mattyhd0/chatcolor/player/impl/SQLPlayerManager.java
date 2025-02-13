package me.mattyhd0.chatcolor.player.impl;

import me.mattyhd0.chatcolor.ChatColorPlugin;
import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import me.mattyhd0.chatcolor.player.AbstractPlayerManager;
import me.mattyhd0.chatcolor.player.CPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public final class SQLPlayerManager extends AbstractPlayerManager {

    public SQLPlayerManager(final @NotNull ChatColorPlugin plugin) {
        super(plugin);
    }

    @Override
    protected CompletionStage<Boolean> savePlayer(@NotNull UUID uuid, @Nullable BasePattern pattern) {
        return CompletableFuture.supplyAsync(() -> {
            final String query = (pattern == null) ? "DELETE FROM playerdata WHERE uuid=?"
                    : "INSERT INTO playerdata(uuid, pattern) VALUES(?,?) ON DUPLICATE KEY UPDATE pattern= VALUES(pattern)";
            try (final var conn = plugin.getConnectionPool().getConnection();
                 final var ps = conn.prepareStatement(query)) {
                ps.setString(1, uuid.toString());
                if (pattern != null) {
                    ps.setString(2, pattern.getName(false));
                }
                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                return false;
            }
        }, executorService);
    }

    @Override
    protected CompletionStage<CPlayer> loadPlayer(@NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try (final var conn = plugin.getConnectionPool().getConnection();
                 final var ps = conn.prepareStatement("SELECT * FROM playerdata WHERE uuid=?")) {
                ps.setString(1, uuid.toString());
                try (final var rs = ps.executeQuery()) {
                    if (!rs.next()) return new CPlayer(null);

                    final var basePattern = plugin.getPatternManager().getPatternByName(rs.getString("pattern"));
                    return new CPlayer(basePattern);
                }
            } catch (SQLException e) {
                return new CPlayer(null);
            }
        }, executorService);
    }
}
