package me.mattyhd0.chatcolor.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.mattyhd0.chatcolor.ChatColorPlugin;
import me.mattyhd0.chatcolor.player.CPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChatColorPlaceholders extends PlaceholderExpansion {


    private ChatColorPlugin plugin;

    public ChatColorPlaceholders() {
        this.plugin = ChatColorPlugin.getInstance();
    }

    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return "MattyHD0";
    }

    public String getIdentifier() {
        return "chatcolor";
    }

    public String getRequiredPlugin() {
        return "ChatColor";
    }

    public String getVersion() {
        return "1.0";
    }

    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(@Nullable Player player, @NotNull String identifier) {
        if(player == null) return "";

        CPlayer cPlayer = plugin.getDataMap().get(player.getUniqueId());

        if (cPlayer == null) return "";

        return switch (identifier) {
            case "pattern_name" -> cPlayer.getPattern() == null ? "" : cPlayer.getPattern().getName(false);

            case "pattern_name_formatted" -> cPlayer.getPattern() == null ? "" : cPlayer.getPattern().getName(true);

            case "kyori_pattern" -> {
                var name = cPlayer.getPattern().getName(false);
                if(name == null) yield "";
                var color = plugin.getConfigurationManager().getPatterns().getString(name + ".kyori");
                yield color == null ? "" : color;
            }
            default -> "";
        };

    }

}
