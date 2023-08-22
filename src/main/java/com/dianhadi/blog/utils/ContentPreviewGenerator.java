package com.dianhadi.blog.utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ContentPreviewGenerator {
    public static String generatePreviewContent(String fullContent) {
        // Remove HTML tags
        fullContent = removeHtmlTags(fullContent);

        // Remove leading and trailing spaces
        fullContent = fullContent.trim();

        // Limit to a maximum of 200 characters
        if (fullContent.length() > 200) {
            fullContent = fullContent.substring(0, 200);

            // Find the last space within the trimmed content
            int lastSpaceIndex = fullContent.lastIndexOf(" ");
            if (lastSpaceIndex != -1) {
                fullContent = fullContent.substring(0, lastSpaceIndex);
            }
        }

        // Remove trailing punctuation
        fullContent = fullContent.replaceAll("[.,;!?]+$", "");

        return fullContent;
    }

    private static String removeHtmlTags(String input) {
        Pattern htmlTagPattern = Pattern.compile("<[^>]+>");
        Matcher matcher = htmlTagPattern.matcher(input);
        return matcher.replaceAll("");
    }
}
