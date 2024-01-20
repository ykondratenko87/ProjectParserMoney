package service;

import exception.ErrorMessages;
import exception.MoneyTransferException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class AccountService implements Constants {

    private static final String BANKS_ACCOUNTS_FILE = Constants.BANKS_ACCOUNTS_FILE_PATH;

    public static Map<String, Integer> readBanksAccounts() throws MoneyTransferException {
        Map<String, Integer> banksAccounts = new HashMap<>();
        try (Stream<String> lines = Files.lines(Path.of(BANKS_ACCOUNTS_FILE))) {
            lines.forEach(line -> {
                String[] parts = line.split("\\s+");
                if (parts.length == 2) {
                    banksAccounts.put(parts[0], Integer.parseInt(parts[1]));
                }
            });
        } catch (IOException e) {
            throw new MoneyTransferException(ErrorMessages.READ_ACCOUNTS_ERROR_MESSAGE, e);
        }
        return banksAccounts;
    }

    public static void updateBanksAccounts(Map<String, Integer> banksAccounts) throws MoneyTransferException {
        try {
            Files.write(Path.of(BANKS_ACCOUNTS_FILE), () ->
                    banksAccounts.entrySet().stream().<CharSequence>map(entry -> entry.getKey() + " " + entry.getValue()).iterator());
        } catch (IOException e) {
            throw new MoneyTransferException(ErrorMessages.UPDATE_ACCOUNTS_ERROR_MESSAGE, e);
        }
    }
}