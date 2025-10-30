package org.example.utils;

import org.example.exceptions.InvalidInputException;
import org.example.exceptions.TooManyAttemptsException;
import java.util.Scanner;

public class InputHelper {
    public static final Scanner scanner = new Scanner(System.in);
    public static int readPositiveInt(String msg) throws TooManyAttemptsException {
        int attempts = 0;
        while(true){
            System.out.print(msg);
            try{
                String input = scanner.nextLine();
                if (!input.matches("\\d+")) throw new InvalidInputException("Unesi samo broj!");
                int value = Integer.parseInt(input);
                if (value <= 0) throw new InvalidInputException("Broj mora biti pozitivan!");
                return value;
            }catch (InvalidInputException e){
                attempts++;
                System.out.println("Greska: " + e.getMessage());
                if(attempts >= 3) throw new TooManyAttemptsException("Previse pogresnih pokusaja.");
            }
        }
    }
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
