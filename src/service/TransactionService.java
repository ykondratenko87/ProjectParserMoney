package service;

import exception.*;
import model.Account;
import model.Transaction;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TransactionService implements Constants {

    private static final String REPORT_FILE = Constants.REPORT_FILE_PATH;

    public List<Transaction> parseFilesInDirectory(String directoryPath) throws MoneyTransferException {
        List<Transaction> transactions = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Path.of(directoryPath), "*.txt")) {
            for (Path filePath : directoryStream) {
                List<Transaction> fileTransactions = parseFile(filePath);
                transactions.addAll(fileTransactions);
            }
        } catch (IOException e) {
            throw new MoneyTransferException(ErrorMessages.FILE_PARSING_ERROR_MESSAGE, e);
        }
        return transactions;
    }

    private List<Transaction> parseFile(Path filePath) throws MoneyTransferException {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String fileName = filePath.getFileName().toString();
            List<Account> accounts = parseAccountData(reader);
            if (accounts.isEmpty()) {
                transactions.add(new Transaction(fileName, "ошибка в формате транзакции", new Date()));
            } else {
                processAccounts(accounts);
                transactions.add(new Transaction(fileName, "успешно", new Date()));
            }
        } catch (IOException e) {
            throw new MoneyTransferException(ErrorMessages.FILE_PARSING_IO_ERROR_MESSAGE + filePath, e);
        } catch (CustomException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    private List<Account> parseAccountData(BufferedReader reader) throws IOException, CustomException {
        List<Account> accounts = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\s+");
            if (parts.length >= 3) {
                String from = parts[0];
                String to = parts[1];
                int amount;
                if (isValidAccountNumber(from) || isValidAccountNumber(to)) {
                    throw new CustomException(ErrorMessages.INVALID_ACCOUNT_NUMBER_FORMAT + line);
                }
                try {
                    amount = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    throw new CustomException(ErrorMessages.INVALID_TRANSFER_AMOUNT_FORMAT + line);
                }
                accounts.add(new Account(from, to, amount));
            }
        }
        return accounts;
    }

    private void processAccounts(List<Account> accounts) throws CustomException, MoneyTransferException {
        Map<String, Integer> banksAccounts = AccountService.readBanksAccounts();
        for (Account account : accounts) {
            processSingleAccount(account, banksAccounts);
        }
        AccountService.updateBanksAccounts(banksAccounts);
    }

    private void processSingleAccount(Account account, Map<String, Integer> banksAccounts) {
        String from = account.getFrom();
        String to = account.getTo();
        int amount = account.getAmount();

        if (!banksAccounts.containsKey(from) || !banksAccounts.containsKey(to)) {
            handleAccountNotFound();
            return;
        }
        int balanceFrom = banksAccounts.get(from);
        int balanceTo = banksAccounts.get(to);
        if (balanceFrom < amount) {
            handleInsufficientFunds();
            return;
        }
        banksAccounts.put(from, balanceFrom - amount);
        banksAccounts.put(to, balanceTo + amount);
    }

    private void handleAccountNotFound() {
        System.err.println(ErrorMessages.ACCOUNT_NOT_FOUND);
    }

    private void handleInsufficientFunds() {
        System.err.println(ErrorMessages.INSUFFICIENT_FUNDS);
    }

    public void writeReportToFile(List<Transaction> transactions) throws MoneyTransferException {
        try (FileWriter writer = new FileWriter(REPORT_FILE)) {
            for (Transaction transaction : transactions) {
                writer.write(transaction + "\n");
            }
        } catch (IOException e) {
            throw new MoneyTransferException(ErrorMessages.REPORT_WRITING_ERROR_MESSAGE + REPORT_FILE, e);
        }
    }

    public List<Transaction> loadAllTransactions() {
        return new ArrayList<>();
    }

    private boolean isValidAccountNumber(String accountNumber) {
        return !accountNumber.matches("\\d{5}-\\d{5}");
    }

    public void printTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
}