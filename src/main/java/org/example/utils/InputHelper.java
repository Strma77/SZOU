package org.example.utils;

import org.example.enums.CourseLevel;
import org.example.enums.LessonType;
import org.example.enums.Semester;
import org.example.exceptions.InvalidInputException;
import org.example.exceptions.NegativeValueException;
import org.example.exceptions.TooManyAttemptsException;
import java.util.Scanner;

/**
 * Provides utility methods for reading and validating user input from the console.
 * Includes support for enums, positive integers, and non-empty strings.
 */
public class InputHelper {

    public static final Scanner scanner = new Scanner(System.in);

    public static int readPositiveInt(String msg) throws TooManyAttemptsException {
        int attempts = 0;
        while(true){
            System.out.print(msg);
            try{
                String input = scanner.nextLine();
                if (!input.matches("\\d+"))
                    throw new InvalidInputException("Unesi samo broj!");
                int value = Integer.parseInt(input);
                if (value < 0)
                    throw new NegativeValueException("Broj mora biti pozitivan!");
                return value;
            }catch (InvalidInputException | NegativeValueException e){
                attempts++;
                System.out.println("Greska: " + e.getMessage());
                if(attempts >= 3)
                    throw new TooManyAttemptsException("Previse pogresnih pokusaja.");
            }
        }
    }

    public static String readNonEmptyString(String msg) throws TooManyAttemptsException {
        int attempts = 0;
        while (true) {
            System.out.print(msg);
            String input = scanner.nextLine().trim();
            try {
                if (input.isEmpty())
                    throw new InvalidInputException("Ne smije biti prazno!");
                return input;
            } catch (InvalidInputException e) {
                attempts++;
                System.out.println("Greška: " + e.getMessage());
                if (attempts >= 3) {
                    throw new TooManyAttemptsException("Previše pogrešnih pokušaja.");
                }
            }
        }
    }

    public static Semester readSemester(String msg) throws TooManyAttemptsException {
        int attempts = 0;
        while (true) {
            System.out.print(msg);
            try {
                String input = scanner.nextLine().trim();
                if (!input.matches("\\d+"))
                    throw new InvalidInputException("Unesi broj između 1 i 6!");

                int num = Integer.parseInt(input);
                return Semester.fromNumber(num);
            } catch (InvalidInputException | IllegalArgumentException e) {
                attempts++;
                System.out.println("Greška: " + e.getMessage());
                if (attempts >= 3) {
                    throw new TooManyAttemptsException("Previše pogrešnih pokušaja.");
                }
            }
        }
    }

    public static CourseLevel readCourseLevel(String msg) throws TooManyAttemptsException {
        System.out.println("Course Levels:");
        CourseLevel[] levels = CourseLevel.values();
        for (int i = 0; i < levels.length; i++) {
            System.out.println((i + 1) + ": " + levels[i].getDisplayName());
        }

        int attempts = 0;
        while (true) {
            System.out.print(msg);
            try {
                String input = scanner.nextLine().trim();
                if (!input.matches("\\d+"))
                    throw new InvalidInputException("Unesi broj!");

                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > levels.length)
                    throw new InvalidInputException("Unesi broj između 1 i " + levels.length);

                return levels[choice - 1];
            } catch (InvalidInputException e) {
                attempts++;
                System.out.println("Greška: " + e.getMessage());
                if (attempts >= 3) {
                    throw new TooManyAttemptsException("Previše pogrešnih pokušaja.");
                }
            }
        }
    }

    public static LessonType readLessonType(String msg) throws TooManyAttemptsException {
        System.out.println("Lesson Types:");
        LessonType[] types = LessonType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ": " + types[i].getDisplayName());
        }

        int attempts = 0;
        while (true) {
            System.out.print(msg);
            try {
                String input = scanner.nextLine().trim();
                if (!input.matches("\\d+"))
                    throw new InvalidInputException("Unesi broj!");

                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > types.length)
                    throw new InvalidInputException("Unesi broj između 1 i " + types.length);

                return types[choice - 1];
            } catch (InvalidInputException e) {
                attempts++;
                System.out.println("Greška: " + e.getMessage());
                if (attempts >= 3) {
                    throw new TooManyAttemptsException("Previše pogrešnih pokušaja.");
                }
            }
        }
    }

    /**
     * Reads a choice from a numbered list (1-based indexing).
     *
     * @param msg the prompt message
     * @param maxChoice the maximum valid choice number
     * @return the selected index (0-based)
     * @throws TooManyAttemptsException if invalid input provided 3 times
     */
    public static int readChoice(String msg, int maxChoice) throws TooManyAttemptsException {
        int attempts = 0;
        while (true) {
            System.out.print(msg);
            try {
                String input = scanner.nextLine().trim();
                if (!input.matches("\\d+"))
                    throw new InvalidInputException("Unesi broj!");

                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > maxChoice)
                    throw new InvalidInputException("Unesi broj između 1 i " + maxChoice);

                return choice - 1; // Convert to 0-based index
            } catch (InvalidInputException e) {
                attempts++;
                System.out.println("Greška: " + e.getMessage());
                if (attempts >= 3) {
                    throw new TooManyAttemptsException("Previše pogrešnih pokušaja.");
                }
            }
        }
    }
}