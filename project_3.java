// Подключение необходимых библиотек
package com.test.idea;
import java.io.BufferedWriter;//предоставляет средства для эффективной записи символьных данных в буфер с последующей записью в файл.
import java.io.FileWriter; //предоставляет возможность записи символьных данных в файл
import java.io.IOException;//используется для обработки исключений, связанных с вводом-выводом.
import java.sql.*;
import java.util.Scanner;//предоставляет удобные методы для считывания данных с консоли и других источников ввода.

// Основной класс программы
public class project_3 {
    public static void main(String[] args) {
        // Инициализация сканнера для ввода с клавиатуры
        Scanner scan = new Scanner(System.in);
        // Инициализация переменных для выбора и имени таблицы
        int choice = 0;
        String tableName = "";

        // Основной цикл программы
        while (choice != 4) {
            // Вывод меню на экран
            printMenu();
            System.out.print("Выберите действие (1-4): ");
            // Ввод выбора пользователя
            choice = scan.nextInt();

            // Обработка выбора пользователя
            switch (choice) {
                // Вывод всех таблиц в базе данных MySQL
                case 1:
                    showMySQLTables();
                    break;
                // Создание новой таблицы в базе данных MySQL
                case 2:
                    System.out.print("Введите название таблицы для создания: ");
                    //будет считывать следующее введенное пользователем слово после вызова метода scan.next().
                    tableName = scan.next();
                    createMySQLTable(tableName);
                    break;
                // Проверка введенных чисел и сохранение в MySQL
                case 3:
                    performNumberCheckAndSave(scan, tableName);
                    break;
                // Сохранение данных в CSV (Excel) файл
                case 4:
                    saveDataToCSV(scan, tableName);
                    break;
                // Обработка некорректного выбора
                default:
                    System.out.println("Неверный выбор. Повторите попытку.");
            }
        }
    }

    // Выводит меню действий на экран
    private static void printMenu() {
        System.out.println("1. Вывести все таблицы из MySQL.");
        System.out.println("2. Создать таблицу в MySQL.");
        System.out.println("3. Выполнение проверки чисел и сохранение в MySQL.");
        System.out.println("4. Сохранить в Excel.");
    }

    // Выводит список всех таблиц в базе данных MySQL
    private static void showMySQLTables() {
        try {
            // Регистрация драйвера JDBC и подключение к базе данных
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String mysqlUrl = "jdbc:mysql://localhost/test10";
            Connection con = DriverManager.getConnection(mysqlUrl, "root", "a07omu");
            // Получение метаданных базы данных
            DatabaseMetaData metaData = con.getMetaData();

            // Получение списка всех таблиц
            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});

            System.out.println("Таблицы в базе данных: ");
            // Вывод названий таблиц на экран
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println(tableName);
            }

            // Закрытие ресурсов
            tables.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Создает новую таблицу в базе данных MySQL
    private static void createMySQLTable(String tableName) {
        try {
            // Регистрация драйвера JDBC и подключение к базе данных
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String mysqlUrl = "jdbc:mysql://localhost/test10";
            Connection con = DriverManager.getConnection(mysqlUrl, "root", "a07omu");
            Statement stmt = con.createStatement();

            // Формирование и выполнение SQL-запроса на создание таблицы
            String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (number INT, isEven BOOLEAN)";
            stmt.executeUpdate(query);

            System.out.println("Таблица " + tableName + " успешно создана в MySQL.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Проверяет введенные числа и сохраняет их в MySQL
    private static void performNumberCheckAndSave(Scanner scan, String tableName) {
        System.out.print("Введите несколько чисел через пробел: ");
        scan.nextLine();

        // Считывание введенной строки и разделение на массив чисел
        String input = scan.nextLine();
        String[] numbers = input.split("\\s+");

        // Обработка каждого числа из массива
        for (String numberStr : numbers) {
            try {
                // Преобразование строки в целое число
                int number = Integer.parseInt(numberStr);
                // Сохранение информации о числе в MySQL
                saveNumberInfoToMySQL(number, tableName);
                System.out.println("Число " + number + " успешно проверено и сохранено в MySQL.");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: '" + numberStr + "' не является целым числом.");
            }
        }
    }

    // Сохраняет информацию о числе в таблицу MySQL
    private static void saveNumberInfoToMySQL(int number, String tableName) {
        try {
            // Регистрация драйвера JDBC и подключение к базе данных
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String mysqlUrl = "jdbc:mysql://localhost/test10";
            Connection con = DriverManager.getConnection(mysqlUrl, "root", "a07omu");
            Statement stmt = con.createStatement();

            // Формирование и выполнение SQL-запроса на добавление записи в таблицу
            String query = "INSERT INTO " + tableName + " (number, isEven) VALUES (" + number + ", " + (number % 2 == 0) + ")";
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Сохраняет данные из таблицы в CSV (Excel) файл
    private static void saveDataToCSV(Scanner scan, String tableName) {
        try {
            // Регистрация драйвера JDBC и подключение к базе данных
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String mysqlUrl = "jdbc:mysql://localhost/test10";
            Connection con = DriverManager.getConnection(mysqlUrl, "root", "a07omu");
            Statement stmt = con.createStatement();

            // Выполнение SQL-запроса для выборки всех данных из таблицы
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + tableName);

            // Формирование содержимого CSV файла
            StringBuilder csvContent = new StringBuilder();
            csvContent.append("Number,IsEven").append(System.lineSeparator());

            // Запись данных из ResultSet в строку CSV
            while (resultSet.next()) {
                csvContent.append(resultSet.getInt("number")).append(",").append(resultSet.getBoolean("isEven")).append(System.lineSeparator());
            }

            // Запрос у пользователя названия файла и сохранение данных в файл
            System.out.print("Введите название файла для сохранения (без расширения): ");
            String fileName = scan.next();
            String filePath = fileName + ".csv";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(csvContent.toString());
                System.out.println("Данные успешно сохранены в файле: " + filePath);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}


//Метод next() считывает следующее слово (последовательность символов без пробелов) из ввода.
//scan.nextLine(): считывает всю строку текста до символа новой строки
//scan.nextInt() - используется для чтения целого числа из ввода. Вот основные особенности метода nextInt():

//Полиморфизм позволяет использовать объекты различных классов как объекты одного общего типа