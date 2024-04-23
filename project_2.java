package com.test.idea;
import java.sql.*;
import java.util.Scanner;

public class project_2 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int choice = 0;
        String tableName = "";
        String inputString1 = "";
        String inputString2 = "";

        while (choice != 9) {
            printMenu();
            System.out.print("Выберите действие (1-9): ");
            choice = scan.nextInt();

            switch (choice) {
                case 1:
                    showMySQLTables();
                    break;
                case 2:
                    System.out.print("Введите название таблицы для создания: ");
                    tableName = scan.next();
                    createMySQLTable(tableName);
                    break;
                case 3:
                    inputString1 = inputString("Введите первую строку (не менее 50 символов): ");
                    inputString2 = inputString("Введите вторую строку (не менее 50 символов): ");
                    saveStringsToMySQL(inputString1, inputString2, tableName);
                    break;
                case 4:
                    printStringLengths(inputString1, inputString2);
                    break;
                case 5:
                    String mergedString = mergeStrings(inputString1, inputString2);
                    saveStringToMySQL(mergedString, tableName);
                    break;
                case 6:
                    compareStrings(inputString1, inputString2);
                    break;
                case 9:
                    System.out.println("Выход из программы.");
                    break;
                default:
                    System.out.println("Неверный выбор. Повторите попытку.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("1. Вывести все таблицы из MySQL.");
        System.out.println("2. Создать таблицу в MySQL.");
        System.out.println("3. Ввести две строки с клавиатуры, результат сохранить в MySQL с последующим выводом в консоль.");
        System.out.println("4. Подсчитать размер ранее введенных строк, результат сохранить в MySQL с последующим выводом в консоль.");
        System.out.println("5. Объединить две строки в единое целое, результат сохранить в MySQL с последующим выводом в консоль.");
        System.out.println("6. Сравнить две ранее введенные строки, результат сохранить в MySQL с последующим выводом в консоль.");
        System.out.println("9. Выйти из программы.");
    }

    private static void showMySQLTables() {

        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String mysqlUrl = "jdbc:mysql://localhost/test1000";
            Connection con = DriverManager.getConnection(mysqlUrl, "root", "a07omu");
            DatabaseMetaData metaData = con.getMetaData();

            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});

            System.out.println("Таблицы в базе данных: ");
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println(tableName);
            }

            tables.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createMySQLTable(String tableName) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String mysqlUrl = "jdbc:mysql://localhost/test1000";
            Connection con = DriverManager.getConnection(mysqlUrl, "root", "a07omu");
            Statement stmt = con.createStatement();

            String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (result DOUBLE)";
            stmt.executeUpdate(query);

            System.out.println("Таблица " + tableName + " успешно создана в MySQL.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String inputString(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static void printStringLengths(String str1, String str2) {
        System.out.println("Длина первой строки: " + str1.length());
        System.out.println("Длина второй строки: " + str2.length());
    }

    private static String mergeStrings(String str1, String str2) {
        return str1 + str2;
    }

    private static void compareStrings(String str1, String str2) {
        if (str1.equals(str2)) {
            System.out.println("Строки идентичны.");
        } else {
            System.out.println("Строки различны.");
        }
    }

    private static void saveStringsToMySQL(String str1, String str2, String tableName) {
        saveStringToMySQL(str1, tableName);
        saveStringToMySQL(str2, tableName);
    }

    private static void saveStringToMySQL(String str, String tableName) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String mysqlUrl = "jdbc:mysql://localhost/test1000";
            Connection con = DriverManager.getConnection(mysqlUrl, "root", "a07omu");
            Statement stmt = con.createStatement();

            String query = "INSERT INTO " + tableName + " (result) VALUES ('" + str + "')";
            stmt.executeUpdate(query);

            System.out.println("Строка успешно сохранена в MySQL.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
