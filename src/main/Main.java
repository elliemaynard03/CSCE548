package main;

import main.java.dao.*;
import main.java.model.*;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final UserDAO userDAO = new UserDAO();
    private static final HabitDAO habitDAO = new HabitDAO();
    private static final DailyLogDAO dailyLogDAO = new DailyLogDAO();
    private static final HabitEntryDAO habitEntryDAO = new HabitEntryDAO();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Wellness & Habit Tracker ===");
            System.out.println("1) List users");
            System.out.println("2) List habits for user");
            System.out.println("3) List daily logs for user");
            System.out.println("4) Show habit entries for a daily log (pretty)");
            System.out.println("5) Add a new habit");
            System.out.println("6) Update a habit target + active");
            System.out.println("7) Delete a habit");
            System.out.println("0) Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> listUsers();
                    case "2" -> {
                        System.out.print("Enter user_id: ");
                        int userId = Integer.parseInt(sc.nextLine());
                        listHabitsForUser(userId);
                    }
                    case "3" -> {
                        System.out.print("Enter user_id: ");
                        int userId = Integer.parseInt(sc.nextLine());
                        listDailyLogsForUser(userId);
                    }
                    case "4" -> {
                        System.out.print("Enter daily_log_id: ");
                        int dailyLogId = Integer.parseInt(sc.nextLine());
                        showEntriesPretty(dailyLogId);
                    }
                    case "5" -> addHabit(sc);
                    case "6" -> updateHabit(sc);
                    case "7" -> deleteHabit(sc);
                    case "0" -> {
                        System.out.println("Bye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void listUsers() throws Exception {
        List<AppUser> users = userDAO.getAll(50);
        for (AppUser u : users) System.out.println(u);
    }

    private static void listHabitsForUser(int userId) throws Exception {
        List<Habit> habits = habitDAO.getByUser(userId);
        for (Habit h : habits) System.out.println(h);
    }

    private static void listDailyLogsForUser(int userId) throws Exception {
        List<DailyLog> logs = dailyLogDAO.getByUser(userId);
        for (DailyLog d : logs) System.out.println(d);
    }

    private static void showEntriesPretty(int dailyLogId) throws Exception {
        List<String> rows = habitEntryDAO.getEntriesPrettyByDailyLog(dailyLogId);
        if (rows.isEmpty()) System.out.println("(none found)");
        for (String r : rows) System.out.println(r);
    }

    private static void addHabit(Scanner sc) throws Exception {
        System.out.print("user_id: ");
        int userId = Integer.parseInt(sc.nextLine());
        System.out.print("name: ");
        String name = sc.nextLine();
        System.out.print("unit (count/minutes/hours/steps/oz/cups/servings/pages): ");
        String unit = sc.nextLine();
        System.out.print("target_value (blank for null): ");
        String tv = sc.nextLine().trim();
        Double targetValue = tv.isEmpty() ? null : Double.parseDouble(tv);

        Habit habit = new Habit(0, userId, name, unit, targetValue, true);
        Habit created = habitDAO.create(habit);
        System.out.println("Created: " + created);
    }

    private static void updateHabit(Scanner sc) throws Exception {
        System.out.print("habit_id: ");
        int habitId = Integer.parseInt(sc.nextLine());
        System.out.print("new target_value (blank for null): ");
        String tv = sc.nextLine().trim();
        Double targetValue = tv.isEmpty() ? null : Double.parseDouble(tv);

        System.out.print("is_active (true/false): ");
        boolean active = Boolean.parseBoolean(sc.nextLine().trim());

        boolean ok = habitDAO.updateTarget(habitId, targetValue, active);
        System.out.println("Updated: " + ok);
    }

    private static void deleteHabit(Scanner sc) throws Exception {
        System.out.print("habit_id: ");
        int habitId = Integer.parseInt(sc.nextLine());
        boolean ok = habitDAO.delete(habitId);
        System.out.println("Deleted: " + ok);
    }
}

