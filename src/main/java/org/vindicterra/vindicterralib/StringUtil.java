package org.vindicterra.vindicterralib;


import net.md_5.bungee.api.ChatColor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class StringUtil {


    public static final Pattern PATTERN = Pattern.compile("\\p{Alnum}+");
    private static final List<String> FILTER = List.of("nigger", "nigga", "faggot", "whore", "cock", "cunt", "dick", "monkey", "tranny", "dike", "chink", "coon", "troon", "fag");
    private static final Pattern HEX_PATTERN = Pattern.compile("&#[a-fA-F0-9]{6}");

    public static boolean isAlphanumeric(String message) {
        return PATTERN.matcher(message).matches();
    }

    public static String formatHex(String message) {
        Matcher match = HEX_PATTERN.matcher(message);
        while(match.find()) {
            String color = message.substring(match.start(), match.end());
            message = message.replace(color, ChatColor.of(color.substring(1)).toString());
            match = HEX_PATTERN.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

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
}
