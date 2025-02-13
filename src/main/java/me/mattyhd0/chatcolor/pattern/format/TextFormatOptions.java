package me.mattyhd0.chatcolor.pattern.format;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextFormatOptions {

    private final List<ChatColor> validChatColors = Arrays.asList(
            ChatColor.BOLD,
            ChatColor.ITALIC,
            ChatColor.UNDERLINE,
            ChatColor.MAGIC,
            ChatColor.STRIKETHROUGH
    );

    private List<ChatColor> formats;

    public TextFormatOptions(){
        formats = new ArrayList<>();
    }

    public TextFormatOptions(ChatColor... formats){
        this();
        this.formats = Arrays.asList(formats);
    }

    public List<ChatColor> getFormats() {
        return formats;
    }

    public String setFormat(String text) {
        if(text.length() == 0 || formats.size() == 0){
            return text;
        }
        String[] splitText = text.split("");
        StringBuilder formattedText = new StringBuilder();
        for (String character: splitText){
            formattedText.append(
                    setFormat(character.charAt(0))
            );
        }
        return formattedText.toString();

    }

    public String setFormat(char character) {
        if(formats.size() == 0){
            return String.valueOf(character);
        }
        StringBuilder formattedChar = new StringBuilder();
        for (ChatColor format: formats){
            formattedChar.append(format);
        }
        return formattedChar.append(character).toString();
    }

    public static TextFormatOptions fromConfigurationSection(ConfigurationSection configurationSection){

        boolean bold = configurationSection.contains("bold") && configurationSection.getBoolean(".bold");
        boolean italic = configurationSection.contains("italic") && configurationSection.getBoolean(".italic");
        boolean underline = configurationSection.contains("underline") && configurationSection.getBoolean(".underline");
        boolean magic = configurationSection.contains("magic") && configurationSection.getBoolean(".magic");
        boolean strikethrough = configurationSection.contains("strikethrough") && configurationSection.getBoolean(".strikethrough");

        TextFormatOptions textFormatOptions = new TextFormatOptions();
        textFormatOptions.of(bold, italic, underline, magic, strikethrough);

        return textFormatOptions;

    }
    public void of(boolean bold, boolean italic, boolean underline, boolean magic, boolean strikethrough){
        if (bold) formats.add(ChatColor.BOLD);
        if (italic) formats.add(ChatColor.ITALIC);
        if (underline) formats.add(ChatColor.UNDERLINE);
        if (magic) formats.add(ChatColor.MAGIC);
        if (strikethrough) formats.add(ChatColor.STRIKETHROUGH);
    }
    public boolean isFormat(ChatColor chatColor){
        return validChatColors.contains(chatColor);
    }

}
