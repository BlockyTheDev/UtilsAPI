package dev.dontblameme.utilsapi.utils;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParser {

    private static final Pattern hexPattern = Pattern.compile("#([A-Fa-f\\d]{6}|[A-Fa-f\\d]{3})");

    private TextParser() {}

    /**
     * @param message String which should be replaced in
     * @return Message with chat-ready replacement of hex
     */
    public static String parseHex(String message) {
        if(message.isEmpty()) throw new IllegalArgumentException("Message may not be null or empty");

        Matcher m = hexPattern.matcher(message);

        while(m.find()) {
            String s = m.group();

            message = message.replace(s, ChatColor.of(Color.decode(s)) + "");
        }
        return message;
    }

    /**
     *
     * @param message String which should be replaced in
     * @param replaceChar Character which should be translated in the message
     * @return Message with chat-ready replacement of color codes
     */
    public static String parseColorCodes(String message, char replaceChar) {
        return ChatColor.translateAlternateColorCodes(replaceChar, message);
    }


    /**
     *
     * @param message String which should be replaced in
     * @return Message with chat-ready replacement of color codes
     */
    public static String parseColorCodes(String message) {
        return parseColorCodes(message, '&');
    }

    /**
     *
     * @param message String which should be replaced in
     * @return Message with chat-ready replacement of color codes and hex
     */
    public static String parseHexAndCodes(String message) {
        return parseHex(parseColorCodes(message));
    }

}
