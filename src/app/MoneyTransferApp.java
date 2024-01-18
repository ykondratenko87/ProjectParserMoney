package app;

import exception.CustomException;
import exception.MoneyTransferException;
import model.Transaction;
import service.MoneyTransferService;

import java.util.List;
import java.util.Scanner;

public class MoneyTransferApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            int choice;
            do {
                System.out.println("Меню:");
                System.out.println("1. Выполнить парсинг файлов перевода");
                System.out.println("2. Вывести список всех операций");
                System.out.println("3. Выход");
                System.out.print("Выберите операцию (1-3): ");
                choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        System.out.print("Введите путь к каталогу: ");
                        String directoryPath = scanner.nextLine();
                        List<Transaction> transactions = MoneyTransferService.parseFilesInDirectory(directoryPath);
                        MoneyTransferService.writeReportToFile(transactions);
                        System.out.println("Отчет успешно создан в файле report/report.txt");
                        break;
                    case 2:
                        System.out.println("Вывод списка всех операций:");
                        List<Transaction> allTransactions = MoneyTransferService.loadAllTransactions();
                        MoneyTransferService.printTransactions(allTransactions);
                        break;
                    case 3:
                        System.out.println("Выход из программы.");
                        break;
                    default:
                        System.out.println("Некорректный выбор. Повторите попытку.");
                }
            } while (choice != 3);
        } catch (CustomException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } catch (MoneyTransferException e) {
            throw new RuntimeException(e);
        }
    }
}