package com.dianhadi.blog.utils;

public class SeoTitleGenerator {
    
    public static String generateSeoUrl(String title) {
        // Remove special characters and spaces
        String cleanedTitle = title.replaceAll("[^a-zA-Z0-9\\s]", "");

        // Convert to lowercase
        cleanedTitle = cleanedTitle.toLowerCase();

        // Replace spaces with hyphens
        String seoUrl = cleanedTitle.replaceAll("\\s+", "-");

        // Ensure the URL is limited to a reasonable length (e.g., 50 characters)
        if (seoUrl.length() > 50) {
            seoUrl = seoUrl.substring(0, 50);
        }

        return seoUrl;
    }
    
}
