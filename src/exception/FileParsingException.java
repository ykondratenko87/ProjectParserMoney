package exception;

import java.io.IOException;

public class FileParsingException extends CustomException {
    public FileParsingException(String message, IOException e) {
        super(message, e);
    }
}