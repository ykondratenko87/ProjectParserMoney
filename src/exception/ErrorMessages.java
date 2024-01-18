package exception;

public class ErrorMessages {
    public static final String READ_ACCOUNTS_ERROR_MESSAGE = "Ошибка при чтении файла banksAccounts";
    public static final String UPDATE_ACCOUNTS_ERROR_MESSAGE = "Ошибка при обновлении файла banksAccounts";
    public static final String FILE_PARSING_ERROR_MESSAGE = "Ошибка при парсинге файлов в директории";
    public static final String FILE_PARSING_IO_ERROR_MESSAGE = "Ошибка при парсинге файла ";
    public static final String REPORT_WRITING_ERROR_MESSAGE = "Ошибка при записи отчета в файл ";
    public static final String INVALID_ACCOUNT_NUMBER_FORMAT = "Некорректный формат номера счета в строке: ";
    public static final String INVALID_TRANSFER_AMOUNT_FORMAT = "Некорректный формат суммы перевода в строке: ";
    public static final String ACCOUNT_NOT_FOUND = "Такого счета нет. Пропуск операции.";
    public static final String INSUFFICIENT_FUNDS = "Недостаточно средств на счете. Пропуск операции.";
}