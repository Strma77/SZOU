package org.example.utils;

import org.example.enums.CourseLevel;
import org.example.enums.LessonType;
import org.example.enums.Semester;
import org.example.exceptions.InvalidInputException;
import org.example.exceptions.NegativeValueException;
import org.example.exceptions.TooManyAttemptsException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Provides utility methods for reading and validating user input from the console.
 * Includes support for enums, positive integers, and non-empty strings.
 */
public class InputHelper {

    private static final Scanner scanner = new Scanner(System.in);

    public static void closeScanner(){
        if (scanner != null) scanner.close();
    }

    public static int readPositiveInt(String msg) throws TooManyAttemptsException {
        int attempts = 0;
        while(true){
            System.out.print(msg);
            try{
                String input = scanner.nextLine();
                if (!input.matches("\\d+"))
                    throw new InvalidInputException("Enter numbers only!");
                int value = Integer.parseInt(input);
                if (value < 0)
                    throw new NegativeValueException("The number must be positive!");
                return value;
            }catch (InvalidInputException | NegativeValueException e){
                attempts++;
                System.out.println("Error: " + e.getMessage());
                if(attempts >= 3)
                    throw new TooManyAttemptsException("Too many incorrect attempts.");
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
                    throw new InvalidInputException("It must not be empty!");
                return input;
            } catch (InvalidInputException e) {
                attempts++;
                System.out.println("Error: " + e.getMessage());
                if (attempts >= 3) {
                    throw new TooManyAttemptsException("Too many incorrect attempts.");
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
                    throw new InvalidInputException("Enter a number between 1 and 6!");

                int num = Integer.parseInt(input);
                return Semester.fromNumber(num);
            } catch (InvalidInputException | IllegalArgumentException e) {
                attempts++;
                System.out.println("Error: " + e.getMessage());
                if (attempts >= 3) {
                    throw new TooManyAttemptsException("Too many incorrect attempts.");
                }
            }
        }
    }

    public static CourseLevel readCourseLevel(String msg) throws TooManyAttemptsException {
        System.out.println("Course Levels:");
        List<CourseLevel> levels = Arrays.asList(CourseLevel.values());
        for (int i = 0; i < levels.size(); i++) {
            System.out.println((i + 1) + ": " + levels.get(i).getDisplayName());
        }

        int attempts = 0;
        while (true) {
            System.out.print(msg);
            try {
                String input = scanner.nextLine().trim();
                if (!input.matches("\\d+"))
                    throw new InvalidInputException("Enter a number!");

                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > levels.size())
                    throw new InvalidInputException("Enter a number between 1 and " + levels.size());

                return levels.get(choice - 1);
            } catch (InvalidInputException e) {
                attempts++;
                System.out.println("Error: " + e.getMessage());
                if (attempts >= 3) {
                    throw new TooManyAttemptsException("Too many incorrect attempts.");
                }
            }
        }
    }

    public static LessonType readLessonType(String msg) throws TooManyAttemptsException {
        System.out.println("Lesson Types:");
        List<LessonType> types = Arrays.asList(LessonType.values());
        for (int i = 0; i < types.size(); i++) {
            System.out.println((i + 1) + ": " + types.get(i).getDisplayName());
        }

        int attempts = 0;
        while (true) {
            System.out.print(msg);
            try {
                String input = scanner.nextLine().trim();
                if (!input.matches("\\d+"))
                    throw new InvalidInputException("Enter a number!");

                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > types.size())
                    throw new InvalidInputException("Enter a number between 1 and " + types.size());

                return types.get(choice - 1);
            } catch (InvalidInputException e) {
                attempts++;
                System.out.println("Error: " + e.getMessage());
                if (attempts >= 3) {
                    throw new TooManyAttemptsException("Too many incorrect attempts.");
                }
            }
        }
    }

    public static int readChoice(String msg, int maxChoice) throws TooManyAttemptsException {
        int attempts = 0;
        while (true) {
            System.out.print(msg);
            try {
                String input = scanner.nextLine().trim();
                if (!input.matches("\\d+"))
                    throw new InvalidInputException("Enter a number!");

                int choice = Integer.parseInt(input);
                if (choice < 1 || choice > maxChoice)
                    throw new InvalidInputException("Enter a number between 1 and " + maxChoice);

                return choice - 1;
            } catch (InvalidInputException e) {
                attempts++;
                System.out.println("Error: " + e.getMessage());
                if (attempts >= 3) {
                    throw new TooManyAttemptsException("Too many incorrect attempts.");
                }
            }
        }
    }
}