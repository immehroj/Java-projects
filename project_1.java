package com.test.idea;
import java.sql.*;
import java.util.Scanner;

// Основной класс
public class project_1 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int choice = 0;
        String tableName = "";

        // Главный цикл программы
        while (choice != 9) {
            printMenu();
            System.out.print("Выберите действие (1-9): ");
            choice = scan.nextInt();

            // Оператор switch для обработки выбора пользователя
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
                    performMathOperation("сложение", "+", tableName);
                    break;
                case 4:
                    performMathOperation("вычитание", "-", tableName);
                    break;
                case 5:
                    performMathOperation("умножение", "*", tableName);
                    break;
                case 6:
                    performMathOperation("деление", "/", tableName);
                    break;
                case 7:
                    performMathOperation("деление по модулю", "%", tableName);
                    break;
                case 8:
                    performMathOperation("возведение в степень", "^", tableName);
                    break;
                case 9:
                    System.out.println("Выход из программы.");
                    break;
                default:
                    System.out.println("Неверный выбор. Повторите попытку.");
            }

        }
    }

    // Метод для вывода меню
    private static void printMenu() {
        System.out.println("1. Вывести все таблицы из MySQL.");
        System.out.println("2. Создать таблицу в MySQL.");
        System.out.println("3. Сложение чисел.");
        System.out.println("4. Вычитание чисел.");
        System.out.println("5. Умножение чисел.");
        System.out.println("6. Деление чисел.");
        System.out.println("7. Деление чисел по модулю (остаток).");
        System.out.println("8. Возведение числа в степень.");
        System.out.println("9. Выйти из программы.");
    }
    // Метод для вывода таблиц из MySQL
    private static void showMySQLTables() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String mysqlUrl = "jdbc:mysql://localhost/test10";
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
    // Метод для создания таблицы в MySQL
    private static void createMySQLTable(String tableName) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String mysqlUrl = "jdbc:mysql://localhost/test10";
            Connection con = DriverManager.getConnection(mysqlUrl, "root", "a07omu");
            Statement stmt = con.createStatement();

            String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (result DOUBLE)";
            stmt.executeUpdate(query);

            System.out.println("Таблица " + tableName + " успешно создана в MySQL.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для выполнения математических операций
    private static void performMathOperation(String operationName, String operator, String tableName) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Введите два числа для " + operationName + ": ");
        double operand1 = scan.nextDouble();
        double operand2 = scan.nextDouble();
        double result = 0;

        // Выполнение математической операции в зависимости от выбора пользователя
        switch (operator) {
            case "+":
                result = operand1 + operand2;
                break;
            case "-":
                result = operand1 - operand2;
                break;
            case "*":
                result = operand1 * operand2;
                break;
            case "/":
                result = operand1 / operand2;
                break;
            case "%":
                result = operand1 % operand2;
                break;
            case "^":
                result = Math.pow(operand1, operand2);
                break;
            default:
                System.out.println("Неверная операция.");
        }

        // Сохранение результата в MySQL и вывод в консоль
        saveResultToMySQL(result, tableName);
        System.out.println("Результат " + operationName + ": " + result);
    }

// Метод для сохранения результата в MySQL
    private static void saveResultToMySQL(double result, String tableName) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String mysqlUrl = "jdbc:mysql://localhost/test10";
            Connection con = DriverManager.getConnection(mysqlUrl, "root", "a07omu");
            Statement stmt = con.createStatement();

            String query = "INSERT INTO " + tableName + " (result) VALUES (" + result + ")";
            stmt.executeUpdate(query);

            System.out.println("Результат успешно сохранен в MySQL.");
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}


