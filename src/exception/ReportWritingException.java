package exception;

import java.io.IOException;

public class ReportWritingException extends CustomException {
    public ReportWritingException(String message, IOException e) {
        super(message, e);
    }
}