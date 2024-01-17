package exception;

import java.io.IOException;

public class ReadAccountsException extends CustomException {
    public ReadAccountsException(String message, IOException e) {
        super(message, e);
    }
}