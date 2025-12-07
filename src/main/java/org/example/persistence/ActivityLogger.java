package org.example.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogger{

    private static final Logger logger = LoggerFactory.getLogger(ActivityLogger.class);
    private static final String ACTIVITY_FILE = "src/main/resources/activity.xml";
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String action){
        try{
            List<String> lines = loadLines();
            if (!lines.isEmpty() && lines.getLast().contains("</activities>")) {
                lines.removeLast();
            }
            String timestamp = LocalDateTime.now().format(formatter);
            lines.add(String.format("  <activity timestamp=\"%s\">%s</activity>",
                    timestamp, escapeXml(action)));
            lines.add("</activities>");
            Files.write(Paths.get(ACTIVITY_FILE), lines);
            logger.debug("Activity logged: {}", action);
        }catch (Exception e) {
            logger.error("Failed to log activity", e);
        }
    }

    public static void printLog() {
        try {
            if (!Files.exists(Paths.get(ACTIVITY_FILE))) {
                System.out.println("\n⚠️  No activity log found yet.");
                return;
            }

            List<String> lines = Files.readAllLines(Paths.get(ACTIVITY_FILE));
            List<ActivityEntry> entries = new ArrayList<>();

            for (String line : lines) {
                if (line.trim().startsWith("<activity")) {
                    String timestamp = extractAttribute(line, "timestamp");
                    String action = extractContent(line);

                    if (timestamp != null && action != null) {
                        entries.add(new ActivityEntry(
                                LocalDateTime.parse(timestamp, formatter), action));
                    }
                }
            }

            if (entries.isEmpty()) {
                System.out.println("\n⚠️  Activity log is empty.");
                return;
            }

            System.out.println("\n" + "=".repeat(60));
            System.out.println("📋 ACTIVITY LOG");
            System.out.println("=".repeat(60));

            for (ActivityEntry entry : entries) {
                System.out.printf("[%s] %s%n",
                        entry.timestamp().format(formatter), entry.action());
            }

            System.out.println("=".repeat(60));
            System.out.println("Total activities: " + entries.size());
            System.out.println();

        } catch (Exception e) {
            logger.error("Failed to print activity log", e);
            System.err.println("❌ Failed to read activity log: " + e.getMessage());
        }
    }

    public static void clearLog() {
        try {
            Files.deleteIfExists(Paths.get(ACTIVITY_FILE));
            logger.info("Activity log cleared");
            System.out.println("✅ Activity log cleared");
        } catch (IOException e) {
            logger.error("Failed to clear activity log", e);
        }
    }

    private static List<String> loadLines() throws IOException {
        if (!Files.exists(Paths.get(ACTIVITY_FILE))) {
            List<String> initial = new ArrayList<>();
            initial.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            initial.add("<activities>");
            initial.add("</activities>");
            return initial;
        }
        return new ArrayList<>(Files.readAllLines(Paths.get(ACTIVITY_FILE)));
    }
    private static String extractAttribute(String line, String attr) {
        int start = line.indexOf(attr + "=\"");
        if (start == -1) return null;
        start += attr.length() + 2;
        int end = line.indexOf("\"", start);
        return end == -1 ? null : line.substring(start, end);
    }
    private static String extractContent(String line) {
        int start = line.indexOf(">");
        int end = line.lastIndexOf("<");
        if (start == -1 || end == -1 || start >= end) return null;
        return line.substring(start + 1, end);
    }
    private static String escapeXml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}