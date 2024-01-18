package exception;

import java.io.IOException;

public class MoneyTransferException extends IOException {
    public MoneyTransferException(String message, IOException e) {
        super(message, e);
    }
}