package exception;

import java.io.IOException;

public class UpdateAccountsException extends CustomException {
    public UpdateAccountsException(String message, IOException e) {
        super(message, e);
    }
}