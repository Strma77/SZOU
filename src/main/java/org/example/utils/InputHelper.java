package org.example.utils;

import org.example.exceptions.InvalidInputException;
import org.example.exceptions.NegativeValueException;
import org.example.exceptions.TooManyAttemptsException;
import java.util.Scanner;

/**
 * Provides utility methods for reading and validating user input from the console.
 * <p>
 * All methods include validation and retry logic (max 3 attempts).
 *
 * <p><b>Thread Safety:</b> Not thread-safe due to shared {@link Scanner} instance.
 */
public class InputHelper {

    /**
     * Shared scanner instance for reading console input from {@code System.in}.
     */
    public static final Scanner scanner = new Scanner(System.in);

    /**
     * Reads a positive integer with validation and retry logic.
     * <p>
     * Validates numeric input and rejects negative values. Allows up to 3 invalid attempts.
     * Zero (0) is considered valid.
     *
     * @param msg the prompt message to display
     * @return validated positive integer
     * @throws TooManyAttemptsException if invalid input provided 3 times
     */
    public static int readPositiveInt(String msg) throws TooManyAttemptsException {
        int attempts = 0;
        while(true){
            System.out.print(msg);
            try{
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) throw new InvalidInputException("Unesi samo broj!");
                int value = Integer.parseInt(input);
                if (value < 0) throw new NegativeValueException("Broj mora biti pozitivan!");
                return value;
            }catch (InvalidInputException e){
                attempts++;
                System.out.println("Greska: " + e.getMessage());
                if(attempts >= 3) throw new TooManyAttemptsException("Previse pogresnih pokusaja.");
            }
        }
    }

    /**
     * Reads a non-empty string with validation and retry logic.
     * <p>
     * Trims whitespace and validates input is not empty. Allows up to 3 invalid attempts.
     *
     * @param msg the prompt message to display
     * @return validated non-empty string (trimmed)
     * @throws TooManyAttemptsException if empty input provided 3 times
     */
    public static String readNonEmptyString(String msg) throws TooManyAttemptsException {
        int attempts = 0;
        while (true) {
            System.out.print(msg);
            String input = scanner.nextLine().trim();
            try {
                if (input.isEmpty()) throw new InvalidInputException("Ne smije biti prazno!");
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
}