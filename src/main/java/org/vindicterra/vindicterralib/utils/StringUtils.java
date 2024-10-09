package org.vindicterra.vindicterralib.utils;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.ChatColor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * a utility class for string checking and manipulation
 */
@SuppressWarnings("ALL")
public class StringUtils {


    public static final Pattern PATTERN = Pattern.compile("\\p{Alnum}+");
    private static final List<String> FILTER = List.of("nigger", "nigga", "faggot", "whore", "cock", "cunt", "dick", "monkey", "tranny", "dike", "chink", "coon", "troon", "fag");
    private static final Pattern HEX_PATTERN = Pattern.compile("&#[a-fA-F0-9]{6}");

    /**
     * checks if the string is alphanumaric
     * @param message the string to ckeck
     */
    public static boolean isAlphanumeric(String message) {
        return PATTERN.matcher(message).matches();
    }

    /**
     *
     * @param message
     * @return returns a string with the color codes
     */
    public static String formatHex(String message) {
        Matcher match = HEX_PATTERN.matcher(message);
        while(match.find()) {
            String color = message.substring(match.start(), match.end());
            message = message.replace(color, ChatColor.of(color.substring(1)).toString());
            match = HEX_PATTERN.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * check is the string contains slurs and other bad words
     * @param message
     * @return
     */
    public static boolean isFiltered(String message) {
        String fixedMessage = message.toLowerCase()
                .replace("@", "a")
                .replace("3", "e")
                .replace("0", "o")
                .replace("4", "a")
                .replace("1", "i")
                .replace("!", "i")
                .replace("9", "g")
                .replace("5", "s")
                .replace(" ", "")
                .replace(",", "")
                .replace(".", "");

        for(String words : FILTER) {
            if(fixedMessage.contains(words.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public static double round(double value) {
        BigDecimal decimal = BigDecimal.valueOf(value);
        decimal = decimal.setScale(1, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }
    
    public static double round(double value, int places) {
        BigDecimal decimal = BigDecimal.valueOf(value);
        decimal = decimal.setScale(places, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }
    
    /**
     *
     * @param name Item name to format
     * @return Formatted item name in the style of "Grass Block"
     */
    public static String format(final String name) {
        StringBuilder output = new StringBuilder();
        for (String character : name.split("_")) {
            output.append(character.substring(0, 1).toUpperCase()).append(character.substring(1).toLowerCase()).append(" ");
        }
        return output.substring(0, output.length() - 1);
    }
    
    /**
     * @param name Name to remove formatting from
     * @return Unformatted name in the style of "GRASS_BLOCK"
     */
    public static String deformat(final String name) {
        return ((TextComponent) Component.text(name.toUpperCase().replace(" ", "_")).compact()).content();
    }
}
