package org.example.persistence;

import org.example.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BackupManager {

    private static final Logger logger = LoggerFactory.getLogger(BackupManager.class);
    private static final String BACKUP_FILE = "src/main/resources/backup.bin";

    public static void createBackup(Set<Student> students, Set<Professor> professors, List<Course> courses, List<Enrollment> enrollments){
        try{
            BackupData data = new BackupData(students, professors, courses, enrollments);
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(BACKUP_FILE))){
                oos.writeObject(data);
            }
            logger.info("Backup created successfully");
            System.out.println("Backup created: backup.bin");
            ActivityLogger.log("Backup created");
        } catch (IOException e) { logger.error("Failed to create backup", e);
            System.err.println("Backup failed: " + e.getMessage());
        }
    }
    public static boolean restoreFromBackup(Set<Student> students, Set<Professor> professors, List<Course> courses, List<Enrollment> enrollments){
        try{
            if (!Files.exists(Paths.get(BACKUP_FILE))) {
                System.out.println("⚠️  No backup file found!");
                logger.warn("Restore attempted but no backup.bin exists");
                return false;
            }

            BackupData data;
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(BACKUP_FILE))) {
                data = (BackupData) ois.readObject();
            }

            students.clear();
            professors.clear();
            courses.clear();
            enrollments.clear();

            students.addAll(data.students);
            professors.addAll(data.professors);
            courses.addAll(data.courses);
            enrollments.addAll(data.enrollments);
            JsonSaver.saveAll(students, professors, courses, enrollments);

            logger.info("✅ Data restored from backup successfully");
            System.out.println("✅ Data restored from backup!");
            System.out.println("   📚 " + students.size() + " students");
            System.out.println("   👨‍🏫 " + professors.size() + " professors");
            System.out.println("   📖 " + courses.size() + " courses");
            System.out.println("   📝 " + enrollments.size() + " enrollments");
            ActivityLogger.log("Data restored from backup");

            return true;
        } catch (ClassNotFoundException e) {
            logger.error("Class definition not found during restore", e);
            System.err.println("❌ Restore failed: incompatible backup file");
            return false;
        } catch (IOException e) {
            logger.error("Failed to restore from backup", e);
            System.err.println("❌ Restore failed: " + e.getMessage());
            return false;
        }
    }
    public static boolean backupExists() {
        return Files.exists(Paths.get(BACKUP_FILE));
    }

    private record BackupData(Set<Student> students, Set<Professor> professors, List<Course> courses,
                              List<Enrollment> enrollments) implements Serializable {
            @Serial
            private static final long serialVersionUID = 1L;

            private BackupData(Set<Student> students, Set<Professor> professors,
                               List<Course> courses, List<Enrollment> enrollments) {
                this.students = new LinkedHashSet<>(students);
                this.professors = new LinkedHashSet<>(professors);
                this.courses = new ArrayList<>(courses);
                this.enrollments = new ArrayList<>(enrollments);
            }
        }
}
