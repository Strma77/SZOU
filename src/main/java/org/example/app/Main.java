package org.example.app;

import org.example.exceptions.TooManyAttemptsException;
import org.example.persistence.*;
import org.example.utils.AppData;
import org.example.utils.DataInitializer;
import org.example.utils.InputHelper;
import org.example.utils.MenuHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    static void main(String[] args) {
        logger.info("\n=== Online Learning System Started ===");
        ActivityLogger.log("Application started");

        try {
            AppData data = DataInitializer.loadOrCreateData();
            MenuHandler.run(data);

            JsonSaver.saveAll(data.students, data.professors, data.courses, data.enrollments);
            logger.info("✅ Program finished successfully!");

        } catch (TooManyAttemptsException e) {
            logger.error("❌ Program interrupted: {}", e.getMessage());
            System.err.println("Program terminated: " + e.getMessage());
        } catch (Exception e) {
            logger.error("❌ Unexpected error: {}", e.getMessage(), e);
            System.err.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            ActivityLogger.log("Application closed");
            InputHelper.closeScanner();
        }
    }
}