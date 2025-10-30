package org.example.app;

import org.example.entities.*;
import org.example.utils.InputHelper;
import org.example.exceptions.TooManyAttemptsException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {
    static void main(String[] args) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            int profNum = InputHelper.readPositiveInt("Koliko profesora zelite unijeti?: ");
            int studNum = InputHelper.readPositiveInt("Koliko studenata zelite unijeti?: ");

            Professor[] profesori = new Professor[profNum];
            Student[] studenti = new Student[studNum];

            for (int i = 0; i < profNum; i++) {
                String firstName = InputHelper.readNonEmptyString("Unesi ime profesora #" + (i + 1) + ": ");
                String lastName = InputHelper.readNonEmptyString("Unesi prezime profesora: ");
                int ID = 10 + i;
                String username = (firstName.charAt(0) + lastName).toLowerCase();
                String email = username + ID + "@profuni.hr";
                String passwd = ID + "123";
                int maxC = InputHelper.readPositiveInt("Koliko tecajeva taj profesor predaje?: ");

                profesori[i] = new Professor.ProfessorBuilder(firstName, lastName, ID)
                        .username(username)
                        .password(passwd)
                        .email(email)
                        .maxCourses(maxC)
                        .build();
            }

            // Unos studenata
            for (int i = 0; i < studNum; i++) {
                String firstName = InputHelper.readNonEmptyString("Unesi ime studenta #" + (i + 1) + ": ");
                String lastName = InputHelper.readNonEmptyString("Unesi prezime studenta: ");
                int ID = 100 + i;
                String username = (firstName.charAt(0) + lastName).toLowerCase();
                String email = username + ID + "@studuni.hr";
                String passwd = ID + "456";
                int maxC = InputHelper.readPositiveInt("Koliko tecajeva taj student pohadja?: ");

                studenti[i] = new Student.StudentBuilder(firstName, lastName, ID)
                        .username(username)
                        .password(passwd)
                        .email(email)
                        .maxCourses(maxC)
                        .build();
            }

            User[] users = new User[profNum + studNum];
            System.arraycopy(profesori, 0, users, 0, profNum);
            System.arraycopy(studenti, 0, users, profNum, studNum);

            int cNum = InputHelper.readPositiveInt("Koliko tecajeva zelite unijeti?: ");
            Course[] courses = new Course[cNum];

            for (int i = 0; i < cNum; i++) {
                String cName = InputHelper.readNonEmptyString("--- Tecaj #" + (i + 1) + " Naziv: ");

                System.out.println("Odaberi profesora za ovaj tecaj: ");
                int profCount = 0;
                for (User user : users) {
                    if (user instanceof Professor) {
                        System.out.println(profCount + ": " + user.getFirstName() + " " + user.getLastName());
                        profCount++;
                    }
                }
                int chosen = InputHelper.readPositiveInt("Unesi index prof: ");
                Professor prof = null;
                int profCounter = 0;
                for (User u : users) {
                    if (u instanceof Professor) {
                        if (profCounter == chosen) {
                            prof = (Professor) u;
                            break;
                        }
                        profCounter++;
                    }
                }

                int maxLesson = InputHelper.readPositiveInt("Koliko lekcija ima taj tecaj?: ");
                Lesson[] lessons = new Lesson[maxLesson];

                for (int j = 0; j < maxLesson; j++) {
                    String lName = InputHelper.readNonEmptyString("--- Lekcija #" + (j + 1) + " Naziv: ");
                    int len = InputHelper.readPositiveInt("Trajanje (min): ");

                    LocalDate date = null;
                    while (date == null) {
                        try {
                            String dateInput = InputHelper.readNonEmptyString("Unesi datum lekcije (dd-MM-yyyy): ");
                            date = LocalDate.parse(dateInput, dateFormatter);
                        } catch (Exception e) {
                            System.out.println("Neispravan datum! Probaj ponovo.");
                        }
                    }

                    LocalTime startTime = null;
                    while (startTime == null) {
                        try {
                            String timeInput = InputHelper.readNonEmptyString("Unesi vrijeme pocetka lekcije (HH:mm): ");
                            startTime = LocalTime.parse(timeInput, timeFormatter);
                        } catch (Exception e) {
                            System.out.println("Neispravan format! Probaj ponovo.");
                        }
                    }

                    Lesson lesson = new Lesson(lName, len);
                    lesson.schedule(date, startTime.getHour(), startTime.getMinute(), len);
                    lessons[j] = lesson;
                }

                courses[i] = new Course(cName, prof, maxLesson);
                for (Lesson l : lessons) courses[i].addLesson(l);
                if (prof != null) prof.addCourse(cName);
            }

        } catch (TooManyAttemptsException e) {
            System.out.println("Previše pogrešnih pokušaja. Program završava.");
        }
    }
}
