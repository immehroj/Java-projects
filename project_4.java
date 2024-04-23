package com.test.idea; // Объявление пакета

import java.io.BufferedWriter; // Импорт класса для буферизированной записи символов
import java.io.FileWriter; // Импорт класса для записи в файл
import java.io.IOException; // Импорт класса для обработки исключений ввода-вывода
import java.sql.*; // Импорт классов для работы с базой данных SQL
import java.util.Scanner; // Импорт класса для ввода данных с клавиатуры

public class project_4 { // Объявление класса

    private static String tableName; // Объявление статической переменной для имени таблицы
    private static String string1; // Объявление статической переменной для первой строки
    private static String string2; // Объявление статической переменной для второй строки

    public static void main(String[] args) { // Главный метод программы
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
                System.out.println("3. Ввести две строки."); // Вывод на экран пункта меню
                System.out.println("4. Возвращение подстроки по индексам."); // Вывод на экран пункта меню
                System.out.println("5. Перевод строк в верхний и нижний регистры."); // Вывод на экран пункта меню
                System.out.println("6. Поиск подстроки и определение окончания подстроки."); // Вывод на экран пункта меню
                System.out.println("7. Сохранить данные в CSV файл."); // Вывод на экран пункта меню
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
                        enterTwoStrings(); // Ввод двух строк
                        break;
                    case 4:
                        returnSubstringAndSaveToDatabase(stmt); // Получение подстроки и сохранение в базе данных
                        break;
                    case 5:
                        convertToUpperCaseAndLowerCase(stmt); // Преобразование строк в верхний и нижний регистр
                        break;
                    case 6:
                        searchAndSaveSubstring(stmt); // Поиск подстроки и сохранение результата в базе данных
                        break;
                    case 7:
                        saveDataToCSV(new Scanner(System.in), tableName); // Сохранение данных в CSV файл
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
            System.out.print("Введите имя таблицы: "); // Вывод на экран запроса на ввод имени таблицы
            tableName = scanner.nextLine(); // Получение имени таблицы от пользователя

            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + // Формирование SQL-запроса для создания таблицы
                    " (id INT PRIMARY KEY AUTO_INCREMENT, strings VARCHAR(255), ind VARCHAR(255), upper VARCHAR(255), podstr VARCHAR(255))";
            stmt.executeUpdate(createTableSQL); // Выполнение SQL-запроса
            System.out.println("Таблица '" + tableName + "' успешно создана или уже существует."); // Вывод сообщения об успешном создании таблицы
        } catch (SQLException e) { // Обработка исключений SQL
            e.printStackTrace(); // Вывод стека исключений
        }
    }

    private static void enterTwoStrings() { // Метод для ввода двух строк
        Scanner scanner = new Scanner(System.in); // Создание объекта Scanner для ввода данных с клавиатуры
        System.out.print("Введите первую строку: "); // Вывод на экран запроса на ввод первой строки
        string1 = scanner.nextLine(); // Получение первой строки от пользователя
        System.out.print("Введите вторую строку: "); // Вывод на экран запроса на ввод второй строки
        string2 = scanner.nextLine(); // Получение второй строки от пользователя
    }

    private static void returnSubstringAndSaveToDatabase(Statement stmt) { // Метод для получения подстроки и сохранения в базе данных
        try { // Обработка исключений
            Scanner scanner = new Scanner(System.in); // Создание объекта Scanner для ввода данных с клавиатуры
            System.out.print("Введите начальный индекс: "); // Вывод на экран запроса на ввод начального индекса
            int startIndex = scanner.nextInt(); // Получение начального индекса от пользователя
            System.out.print("Введите конечный индекс: "); // Вывод на экран запроса на ввод конечного индекса
            int endIndex = scanner.nextInt(); // Получение конечного индекса от пользователя

            String substring1 = getSubstring(string1, startIndex, endIndex); // Получение подстроки для первой строки
            String substring2 = getSubstring(string2, startIndex, endIndex); // Получение подстроки для второй строки

            String insertSQL1 = "INSERT INTO " + tableName + " (strings, ind) VALUES ('" + string1 + "', '" + substring1 + "')"; // Формирование SQL-запроса для вставки подстроки в базу данных
            stmt.executeUpdate(insertSQL1); // Выполнение SQL-запроса
            String insertSQL2 = "INSERT INTO " + tableName + " (strings, ind) VALUES ('" + string2 + "', '" + substring2 + "')"; // Формирование SQL-запроса для вставки подстроки в базу данных
            stmt.executeUpdate(insertSQL2); // Выполнение SQL-запроса

            System.out.println("Подстроки успешно сохранены в базе данных."); // Вывод сообщения об успешном сохранении подстрок в базе данных
        } catch (SQLException e) { // Обработка исключений SQL
            e.printStackTrace(); // Вывод стека исключений
        }
    }

    private static String getSubstring(String input, int startIndex, int endIndex) { // Метод для получения подстроки
        if (startIndex >= 0 && endIndex < input.length() && startIndex <= endIndex) { // Проверка корректности индексов
            return input.substring(startIndex, endIndex + 1); // Возврат подстроки
        } else {
            System.out.println("Некорректные индексы. Возвращена пустая строка."); // Вывод сообщения об ошибке при некорректных индексах
            return ""; // Возвращение пустой строки
        }
    }

    private static void convertToUpperCaseAndLowerCase(Statement stmt) { // Метод для преобразования строк в верхний и нижний регистр
        try { // Обработка исключений
            String upperCaseString1 = string1.toUpperCase(); // Преобразование первой строки в верхний регистр
            String lowerCaseString1 = string1.toLowerCase(); // Преобразование первой строки в нижний регистр
            String upperCaseString2 = string2.toUpperCase(); // Преобразование второй строки в верхний регистр
            String lowerCaseString2 = string2.toLowerCase(); // Преобразование второй строки в нижний регистр

            if (string1.equals(lowerCaseString1)) { // Проверка, является ли первая строка в нижнем регистре
                updateColumns(stmt, "upper", string1, upperCaseString1); // Обновление столбца в базе данных
            } else {
                updateColumns(stmt, "upper", string1, lowerCaseString1); // Обновление столбца в базе данных
            }

            if (string2.equals(lowerCaseString2)) { // Проверка, является ли вторая строка в нижнем регистре
                updateColumns(stmt, "upper", string2, upperCaseString2); // Обновление столбца в базе данных
            } else {
                updateColumns(stmt, "upper", string2, lowerCaseString2); // Обновление столбца в базе данных
            }

            System.out.println("Строки успешно обновлены в базе данных."); // Вывод сообщения об успешном обновлении строк в базе данных
        } catch (SQLException e) { // Обработка исключений SQL
            e.printStackTrace(); // Вывод стека исключений
        }
    }

    private static void updateColumns(Statement stmt, String columnName, String originalString, String updatedString) throws SQLException { // Метод для обновления столбца в базе данных
        String updateSQL = "UPDATE " + tableName + " SET `" + columnName + "` = '" + updatedString + "' WHERE strings = '" + originalString + "'"; // Формирование SQL-запроса для обновления столбца
        stmt.executeUpdate(updateSQL); // Выполнение SQL-запроса
    }

    private static void searchAndSaveSubstring(Statement stmt) { // Метод для поиска подстроки и сохранения результата в базе данных
        try { // Обработка исключений
            Scanner scanner = new Scanner(System.in); // Создание объекта Scanner для ввода данных с клавиатуры
            System.out.print("Введите подстроку для поиска: "); // Вывод на экран запроса на ввод подстроки для поиска
            String searchSubstring = scanner.nextLine(); // Получение подстроки для поиска от пользователя

            boolean containsSubstring1 = string1.contains(searchSubstring); // Проверка наличия подстроки в первой строке
            boolean containsSubstring2 = string2.contains(searchSubstring); // Проверка наличия подстроки во второй строке

            String result1 = containsSubstring1 ? "true" : "false"; // Преобразование результата поиска в строку для сохранения в базе данных
            String result2 = containsSubstring2 ? "true" : "false"; // Преобразование результата поиска в строку для сохранения в базе данных

            String insertSQL1 = "UPDATE " + tableName + " SET podstr = '" + result1 + "' WHERE strings = '" + string1 + "'"; // Формирование SQL-запроса для сохранения результата поиска в базе данных
            stmt.executeUpdate(insertSQL1); // Выполнение SQL-запроса
            String insertSQL2 = "UPDATE " + tableName + " SET podstr = '" + result2 + "' WHERE strings = '" + string2 + "'"; // Формирование SQL-запроса для сохранения результата поиска в базе данных
            stmt.executeUpdate(insertSQL2); // Выполнение SQL-запроса

            System.out.println("Результаты успешно сохранены в базе данных."); // Вывод сообщения об успешном сохранении результата поиска в базе данных
        } catch (SQLException e) { // Обработка исключений SQL
            e.printStackTrace(); // Вывод стека исключений
        }
    }

    private static void saveDataToCSV(Scanner scan, String tableName) { // Метод для сохранения данных в CSV файл
        try { // Обработка исключений
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver()); // Регистрация драйвера JDBC
            String mysqlUrl = "jdbc:mysql://localhost/test10"; // URL базы данных MySQL
            Connection con = DriverManager.getConnection(mysqlUrl, "root", "a07omu"); // Установка соединения с базой данных
            Statement stmt = con.createStatement(); // Создание объекта для выполнения SQL-запросов

            ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + tableName); // Выполнение запроса на получение данных из таблицы

            System.out.print("Введите название файла для сохранения (без расширения): "); // Вывод на экран запроса на ввод названия файла для сохранения
            String fileName = scan.next(); // Получение названия файла от пользователя
            String filePath = fileName + ".csv"; // Формирование пути к файлу

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) { // Создание объекта для записи в файл
                writer.write("ID,Strings,Ind,Upper,Podstr\n"); // Запись заголовка в файл

                while (resultSet.next()) { // Перебор результатов запроса
                    writer.write(resultSet.getInt("id") + "," + // Запись данных в файл
                            resultSet.getString("strings") + "," +
                            resultSet.getString("ind") + "," +
                            resultSet.getString("upper") + "," +
                            resultSet.getString("podstr") + "\n");
                }

                System.out.println("Данные успешно сохранены в файле: " + filePath); // Вывод сообщения об успешном сохранении данных в файле
            }
        } catch (SQLException | IOException e) { // Обработка исключений SQL и IOException
            e.printStackTrace(); // Вывод стека исключений
        }
    }

}
