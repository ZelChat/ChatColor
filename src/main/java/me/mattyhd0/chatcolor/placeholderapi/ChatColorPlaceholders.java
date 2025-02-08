package me.mattyhd0.chatcolor.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.mattyhd0.chatcolor.CPlayer;
import me.mattyhd0.chatcolor.ChatColorPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ChatColorPlaceholders extends PlaceholderExpansion {
    private Plugin plugin;

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

    public String onPlaceholderRequest(Player player, String identifier) {

        CPlayer cPlayer = player != null ? ChatColorPlugin.getInstance().getDataMap().get(player.getUniqueId()) : null;

        if (cPlayer == null) {
            return "";
        }

        return switch (identifier) {
            case "last_message" -> cPlayer.getLastMessages();

            case "pattern_name" -> cPlayer.getPattern() == null ? "" : cPlayer.getPattern().getName(false);

            case "pattern_name_formatted" -> cPlayer.getPattern() == null ? "" : cPlayer.getPattern().getName(true);

            case "kyori_pattern" -> {
                var name = cPlayer.getPattern().getName(false);
                var color = ChatColorPlugin.getInstance().getConfigurationManager().getPatterns().getString(name + ".kyori");
                yield color == null ? "" : color;
            }
            default -> "";
        };

    }

}
