package me.mattyhd0.chatcolor.util;

import me.clip.placeholderapi.PlaceholderAPI;
import me.mattyhd0.chatcolor.ChatColor;
import me.mattyhd0.chatcolor.configuration.Config;
import me.mattyhd0.chatcolor.pattern.api.IPattern;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Placeholders {

    public static String setPlaceholders(String text, IPattern pattern, Player player){

        FileConfiguration config = Config.getGui();

        if(pattern != null){

            text = text.replaceAll("\\{pattern_name}", pattern.getName(false))
                    .replaceAll("\\{pattern_name_formatted}", pattern.getName(true))
                    .replaceAll("\\{example_text}", pattern.getText(config.getString("gui.colored-example-text")));

        }

        if(player != null){

            if(ChatColor.getInstance().supportPlugin("PlaceholderAPI")){

                text = PlaceholderAPI.setPlaceholders(player, text);

            }

        }

        return text;


    }

    public static List<String> setPlaceholders(List<String> texts, IPattern pattern, Player player){

        if(texts == null) return new ArrayList<>();

        for(int i = 0; i < texts.size(); i++){

            texts.set(i, setPlaceholders(texts.get(i), pattern, player));

        }

        return texts;

    }

}