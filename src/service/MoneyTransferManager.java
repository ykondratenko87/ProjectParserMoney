package service;

import exception.MoneyTransferException;
import model.Transaction;

import java.util.List;
import java.util.Scanner;

public class MoneyTransferManager {

    private final TransactionService transactionService;

    public MoneyTransferManager() {
        this.transactionService = new TransactionService();
    }

    public void processChoice(int choice) throws MoneyTransferException {
        switch (choice) {
            case 1:
                performFileParsing();
                break;
            case 2:
                displayAllTransactions();
                break;
            case 3:
                System.out.println("Выход из программы.");
                break;
            default:
                System.out.println("Некорректный выбор. Повторите попытку.");
        }
    }

    private void performFileParsing() throws MoneyTransferException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите путь к каталогу: ");
        String directoryPath = scanner.nextLine();
        List<Transaction> transactions = transactionService.parseFilesInDirectory(directoryPath);
        transactionService.writeReportToFile(transactions);
        System.out.println("Отчет успешно создан в файле report/report.txt");
    }

    private void displayAllTransactions() {
        System.out.println("Вывод списка всех операций:");
        List<Transaction> allTransactions = transactionService.loadAllTransactions();
        transactionService.printTransactions(allTransactions);
    }
}