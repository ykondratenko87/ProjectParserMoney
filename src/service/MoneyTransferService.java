package service;

import exception.CustomException;
import model.Account;
import model.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoneyTransferService {

    private static final String BANKS_ACCOUNTS_FILE = "src/banksAccounts/accounts.txt";

    public static Map<String, Integer> readBanksAccounts() throws CustomException {
        Map<String, Integer> banksAccounts = new HashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get(BANKS_ACCOUNTS_FILE))) {
            lines.forEach(line -> {
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    banksAccounts.put(parts[0], Integer.parseInt(parts[1]));
                }
            });
        } catch (IOException e) {
            throw new CustomException("Ошибка при чтении файла banksAccounts", e);
        }
        return banksAccounts;
    }

    public static void updateBanksAccounts(Map<String, Integer> banksAccounts) throws CustomException {
        try {
            Files.write(Paths.get(BANKS_ACCOUNTS_FILE), () -> banksAccounts.entrySet().stream().<CharSequence>map(entry -> entry.getKey() + " " + entry.getValue()).iterator());
        } catch (IOException e) {
            throw new CustomException("Ошибка при обновлении файла banksAccounts", e);
        }
    }

    private static final String REPORT_FILE = "src/report/report.txt";

    public static List<Transaction> parseFilesInDirectory(String directoryPath) throws CustomException {
        List<Transaction> transactions = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directoryPath), "*.txt")) {
            for (Path filePath : directoryStream) {
                List<Transaction> fileTransactions = parseFile(filePath);
                transactions.addAll(fileTransactions);
            }
        } catch (IOException e) {
            throw new CustomException("Ошибка при парсинге файлов в директории", e);
        }
        return transactions;
    }

    private static List<Transaction> parseFile(Path filePath) throws CustomException {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String fileName = filePath.getFileName().toString();
            List<Account> accounts = parseAccountData(reader);
            if (accounts.isEmpty()) {
                transactions.add(new Transaction(fileName, "ошибка в формате транзакции", new Date()));
            } else {
                processAccounts(fileName, accounts);
                transactions.add(new Transaction(fileName, "успешно", new Date()));
            }
        } catch (IOException e) {
            throw new CustomException("Ошибка при парсинге файла " + filePath, e);
        }
        return transactions;
    }

    private static List<Account> parseAccountData(BufferedReader reader) throws IOException, CustomException {
        List<Account> accounts = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\s+");
            if (parts.length >= 3) {
                String from = parts[0];
                String to = parts[1];
                int amount;

                if (isValidAccountNumber(from) || isValidAccountNumber(to)) {
                    throw new CustomException("Некорректный формат номера счета в строке: " + line);
                }
                try {
                    amount = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    throw new CustomException("Некорректный формат суммы перевода в строке: " + line);
                }
                accounts.add(new Account(from, to, amount));
            }
        }
        return accounts;
    }

    private static void processAccounts(String fileName, List<Account> accounts) throws CustomException {
        Map<String, Integer> banksAccounts = readBanksAccounts();

        for (Account account : accounts) {
            String from = account.getFrom();
            String to = account.getTo();
            int amount = account.getAmount();

            if (!banksAccounts.containsKey(from) || !banksAccounts.containsKey(to)) {
                System.err.println("Такого счета нет. Пропуск операции.");
                continue;
            }
            int balanceFrom = banksAccounts.get(from);
            int balanceTo = banksAccounts.get(to);
            if (balanceFrom < amount) {
                System.err.println("Недостаточно средств на счете. " + from + "Пропуск.");
                continue;
            }
            banksAccounts.put(from, balanceFrom - amount);
            banksAccounts.put(to, balanceTo + amount);
        }
        updateBanksAccounts(banksAccounts);
    }

    public static void writeReportToFile(List<Transaction> transactions) throws CustomException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REPORT_FILE))) {
            for (Transaction transaction : transactions) {
                writer.println(transaction);
            }
        } catch (IOException e) {
            throw new CustomException("Ошибка при записи отчета в файл " + REPORT_FILE, e);
        }
    }

    public static List<Transaction> loadAllTransactions() throws CustomException {
        return new ArrayList<>();
    }

    private static boolean isValidAccountNumber(String accountNumber) {
        return !accountNumber.matches("\\d{5}-\\d{5}");
    }

    public static void printTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
}