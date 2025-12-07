package org.example.utils;

import org.example.entities.User;
import org.example.exceptions.TooManyAttemptsException;
import org.example.persistence.*;
import org.example.services.*;
import org.example.utils.InputHelper;

import java.util.List;

/**
 * Handles the main menu loop and user interactions.
 */
public class MenuHandler {

    public static void run(AppData data) throws TooManyAttemptsException {
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = InputHelper.readPositiveInt("Choice: ");
            ActivityLogger.log("User selected menu option: " + choice);

            switch (choice) {
                case 1 -> viewStudents(data);
                case 2 -> viewProfessors(data);
                case 3 -> viewCourses(data);
                case 4 -> SearchService.searchLoop(data.students, data.professors, data.courses);
                case 5 -> FeatureDemo.demonstrate(data.students, data.courses, data.enrollments);
                case 6 -> BackupManager.createBackup(data.students, data.professors,
                        data.courses, data.enrollments);
                case 7 -> restoreBackup(data);
                case 8 -> ActivityLogger.printLog();
                case 9 -> ActivityLogger.clearLog();
                case 10 -> resetData(data);
                case 0 -> running = exitAndSave(data);
                default -> System.out.println("❌ Invalid choice!");
            }

            if (choice == 7 || choice == 10) {
                JsonSaver.saveAll(data.students, data.professors, data.courses, data.enrollments);
            }
        }
    }

    private static void displayMenu() {
        System.out.println("""
                
                ╔════════════════════════════════════╗
                ║         MAIN MENU                  ║
                ╚════════════════════════════════════╝
                1  - View All Students
                2  - View All Professors
                3  - View All Courses
                4  - Search (Students/Professors/Courses)
                5  - Show Statistics & Features Demo
                6  - Create Backup
                7  - Restore from Backup
                8  - View Activity Log
                9  - Clear Activity Log
                10 - RESET ALL DATA
                0  - Exit & Save
                """);
    }

    private static void viewStudents(AppData data) {
        List<User> students = data.students.stream().map(s -> (User) s).toList();
        PrintService.printUsers(students, data.courses);
    }

    private static void viewProfessors(AppData data) {
        List<User> professors = data.professors.stream().map(p -> (User) p).toList();
        PrintService.printUsers(professors, data.courses);
    }

    private static void viewCourses(AppData data) {
        System.out.println("\n========== ALL COURSES ==========");
        data.courses.forEach(System.out::println);
        System.out.println("=================================\n");
    }

    private static void restoreBackup(AppData data) {
        if (BackupManager.restoreFromBackup(data.students, data.professors,
                data.courses, data.enrollments)) {
            data.refreshUsers();
        }
    }

    private static void resetData(AppData data) throws TooManyAttemptsException {
        System.out.println("\n⚠️  WARNING: This will delete ALL data!");
        String confirm = InputHelper.readNonEmptyString("Type 'DELETE' to confirm: ");

        if (confirm.equals("DELETE")) {
            data.students.clear();
            data.professors.clear();
            data.courses.clear();
            data.enrollments.clear();
            JsonSaver.deleteAllData();
            System.out.println("🗑️  All data deleted.");
            ActivityLogger.log("User reset all data");
        } else {
            System.out.println("❌ Reset cancelled.");
        }
    }

    private static boolean exitAndSave(AppData data) {
        System.out.println("\n💾 Saving data...");
        JsonSaver.saveAll(data.students, data.professors, data.courses, data.enrollments);
        System.out.println("✅ Goodbye!");
        return false;
    }
}