package app;

import exception.MoneyTransferException;
import service.MoneyTransferManager;

import java.util.Scanner;

public class MoneyTransferApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            MoneyTransferManager manager = new MoneyTransferManager();

            int choice;
            do {
                displayMenu();
                choice = scanner.nextInt();
                scanner.nextLine();
                manager.processChoice(choice);
            } while (choice != 3);
        } catch (MoneyTransferException e) {
            handleException(e);
        }
    }

    private static void displayMenu() {
        System.out.println("Меню:");
        System.out.println("1. Выполнить парсинг файлов перевода");
        System.out.println("2. Вывести список всех операций");
        System.out.println("3. Выход");
        System.out.print("Выберите операцию (1-3): ");
    }

    private static void handleException(MoneyTransferException e) {
        throw new RuntimeException(e);
    }
}