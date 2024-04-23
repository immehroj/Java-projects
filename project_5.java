package com.test.idea; // Объявление пакета

import java.sql.*; // Импорт классов для работы с базой данных SQL
import java.io.FileWriter; // Импорт класса для записи в файл
import java.io.IOException; // Импорт класса для обработки исключений ввода-вывода
import java.sql.ResultSet; // Импорт класса для работы с результатами запросов SQL
import java.sql.SQLException; // Импорт класса для обработки исключений SQL
import java.sql.Statement; // Импорт класса для выполнения SQL-запросов
import java.util.Scanner; // Импорт класса для ввода данных с клавиатуры

public class project_5 { // Объявление класса

    private static String tableName; // Объявление статической переменной для имени таблицы
    private static String string1; // Объявление статической переменной для первой строки
    private static String string2; // Объявление статической переменной для второй строки

    public static void main(String[] args) { // Метод main
        Connection conn = null; // Инициализация переменной для соединения с базой данных
        Statement stmt = null; // Инициализация переменной для выполнения SQL-запросов

        try { // Обработка исключений
            Class.forName("com.mysql.cj.jdbc.Driver"); // Загрузка класса JDBC драйвера
            conn = DriverManager.getConnection("jdbc:mysql://localhost/test10", "root", "a07omu"); // Установка соединения с базой данных
            stmt = conn.createStatement(); // Создание объекта для выполнения SQL-запросов

            int choice; // Объявление переменной для выбора действия пользователем
            do { // Цикл выбора действия
                System.out.println("1. Вывести все таблицы из MySQL."); // Вывод на экран пункта меню
                System.out.println("2. Создать таблицу в MySQL."); // Вывод на экран пункта меню
                System.out.println("3. Изменить порядок символов строки на обратный и добавить одну строку в другую."); // Вывод на экран пункта меню
                System.out.println("4. Сохранить результаты в MySQL и вывести на экран."); // Вывод на экран пункта меню
                System.out.println("5. Сохранить все данные из MySQL в Excel и вывести на экран."); // Вывод на экран пункта меню
                System.out.println("0. Выход"); // Вывод на экран пункта меню

                choice = getUserChoice(); // Получение выбора пользователя

                switch (choice) { // Выбор действия в зависимости от выбора пользователя
                    case 1:
                        displayAllTables(stmt); // Вывод всех таблиц
                        break;
                    case 2:
                        createTable(stmt); // Создание таблицы
                        break;
                    case 3:
                        reverseAndMergeStrings(stmt); // Изменение порядка символов и объединение строк
                        break;
                    case 4:
                        saveResultsToMySQL(stmt); // Сохранение результатов в базу данных и вывод на экран
                        break;
                    case 5:
                        saveToExcelAndDisplay(stmt); // Сохранение результатов в Excel и вывод на экран
                        break;
                    case 0:
                        System.out.println("Программа завершена."); // Вывод сообщения о завершении программы
                        break;
                    default:
                        System.out.println("Некорректный выбор. Пожалуйста, выберите снова."); // Вывод сообщения об ошибке при некорректном выборе
                        break;
                }
            } while (choice != 0); // Повторять, пока пользователь не выберет выход
        } catch (Exception e) { // Обработка исключений
            e.printStackTrace(); // Вывод стека исключений
        } finally { // Блок, который будет выполнен в любом случае после выполнения блока try или catch
            try { // Обработка исключений
                if (stmt != null) stmt.close(); // Закрытие объекта для выполнения SQL-запросов
                if (conn != null) conn.close(); // Закрытие соединения с базой данных
            } catch (SQLException se) { // Обработка исключений SQL
                se.printStackTrace(); // Вывод стека исключений
            }
        }
    }

    private static int getUserChoice() { // Метод для получения выбора пользователя
        Scanner scanner = new Scanner(System.in); // Создание объекта Scanner для ввода данных с клавиатуры
        System.out.print("Введите номер действия: "); // Вывод на экран запроса на ввод номера действия
        return scanner.nextInt(); // Возврат введенного пользователем числа
    }

    private static void displayAllTables(Statement stmt) { // Метод для вывода всех таблиц из базы данных
        try { // Обработка исключений
            ResultSet rs = stmt.executeQuery("SHOW TABLES"); // Выполнение запроса на получение списка таблиц
            System.out.println("Список таблиц в базе данных:"); // Вывод заголовка

            while (rs.next()) { // Перебор результатов запроса
                String tableName = rs.getString(1); // Получение имени таблицы
                System.out.println(tableName); // Вывод имени таблицы
            }
        } catch (SQLException e) { // Обработка исключений SQL
            e.printStackTrace(); // Вывод стека исключений
        }
    }

    private static void createTable(Statement stmt) { // Метод для создания таблицы в базе данных
        try { // Обработка исключений
            Scanner scanner = new Scanner(System.in); // Создание объекта Scanner для ввода данных с клавиатуры
            System.out.print("Введите имя таблицы: "); // Вывод запроса на ввод имени таблицы
            tableName = scanner.nextLine(); // Получение имени таблицы от пользователя

            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + // Формирование SQL-запроса для создания таблицы
                    " (id INT PRIMARY KEY AUTO_INCREMENT, strings VARCHAR(255))"; // Определение структуры таблицы
            stmt.executeUpdate(createTableSQL); // Выполнение SQL-запроса
            System.out.println("Таблица '" + tableName + "' успешно создана или уже существует."); // Вывод сообщения об успешном создании таблицы
        } catch (SQLException e) { // Обработка исключений SQL
            e.printStackTrace(); // Вывод стека исключений
        }
    }

    private static void reverseAndMergeStrings(Statement stmt) { // Метод для изменения порядка символов и объединения строк
        try { // Обработка исключений
            Scanner scanner = new Scanner(System.in); // Создание объекта Scanner для ввода данных с клавиатуры
            System.out.print("Введите первую строку (не менее 50 символов): "); // Вывод запроса на ввод первой строки
            string1 = scanner.nextLine(); // Получение первой строки от пользователя
            System.out.print("Введите вторую строку (не менее 50 символов): "); // Вывод запроса на ввод второй строки
            string2 = scanner.nextLine(); // Получение второй строки от пользователя

            // Изменяем порядок символов на обратный для каждой строки
            StringBuilder reversedString1 = new StringBuilder(string1).reverse(); // Создание объекта StringBuilder для первой строки и изменение порядка символов на обратный
            StringBuilder reversedString2 = new StringBuilder(string2).reverse(); // Создание объекта StringBuilder для второй строки и изменение порядка символов на обратный

            // Соединяем строки в одну
            String mergedString = reversedString1.toString() + reversedString2.toString(); // Объединение строк

            // Сохраняем объединенную строку в базу данных
            String insertSQL = "INSERT INTO " + tableName + " (strings) VALUES ('" + mergedString + "')"; // Формирование SQL-запроса для вставки объединенной строки в базу данных
            stmt.executeUpdate(insertSQL); // Выполнение SQL-запроса

            System.out.println("Строки успешно обработаны и добавлены в базу данных."); // Вывод сообщения об успешном добавлении строки в базу данных
        } catch (SQLException e) { // Обработка исключений SQL
            e.printStackTrace(); // Вывод стека исключений
        }
    }

    private static void saveResultsToMySQL(Statement stmt) { // Метод для сохранения результатов в MySQL и вывода на экран
        try { // Обработка исключений
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName); // Выполнение запроса на выборку всех данных из таблицы
            System.out.println("Результаты:"); // Вывод заголовка

            while (rs.next()) { // Перебор результатов запроса
                String result = rs.getString("strings"); // Получение значения строки из результата запроса
                System.out.println(result); // Вывод строки на экран
            }
        } catch (SQLException e) { // Обработка исключений SQL
            e.printStackTrace(); // Вывод стека исключений
        }
    }

    private static void saveToExcelAndDisplay(Statement stmt) { // Метод для сохранения результатов в Excel и вывода на экран
        try { // Обработка исключений
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName); // Выполнение запроса на выборку всех данных из таблицы
            System.out.println("Результаты:"); // Вывод заголовка

            Scanner scanner = new Scanner(System.in); // Создание объекта Scanner для ввода данных с клавиатуры
            System.out.print("Введите название файла Excel (без расширения): "); // Вывод запроса на ввод имени файла Excel
            String fileName = scanner.nextLine() + ".csv"; // Получение имени файла Excel от пользователя с добавлением расширения .csv

            try (FileWriter writer = new FileWriter(fileName)) { // Создание объекта FileWriter для записи в файл
                writer.write("Strings\n"); // Запись заголовка в файл

                while (rs.next()) { // Перебор результатов запроса
                    String result = rs.getString("strings"); // Получение значения строки из результата запроса
                    writer.write(result + "\n"); // Запись строки в файл
                    System.out.println(result); // Вывод строки на экран
                }

                System.out.println("Данные успешно сохранены в файл: " + fileName); // Вывод сообщения об успешном сохранении данных в файл
            } catch (IOException e) { // Обработка исключений ввода-вывода
                e.printStackTrace(); // Вывод стека исключений
            }
        } catch (SQLException e) { // Обработка исключений SQL
            e.printStackTrace(); // Вывод стека исключений
        }
    }

}
