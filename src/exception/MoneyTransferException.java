package exception;

import java.io.IOException;

public class MoneyTransferException extends IOException {
    public static final String READ_ACCOUNTS_ERROR_MESSAGE = "Ошибка при чтении файла banksAccounts";
    public static final String UPDATE_ACCOUNTS_ERROR_MESSAGE = "Ошибка при обновлении файла banksAccounts";
    public static final String FILE_PARSING_ERROR_MESSAGE = "Ошибка при парсинге файлов в директории";
    public static final String FILE_PARSING_IO_ERROR_MESSAGE = "Ошибка при парсинге файла ";
    public static final String REPORT_WRITING_ERROR_MESSAGE = "Ошибка при записи отчета в файл ";

    public MoneyTransferException(String message, IOException e) {
        super(message, e);
    }
}