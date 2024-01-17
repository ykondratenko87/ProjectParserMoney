package exception;

import java.io.IOException;

public class FileParsingIOException extends CustomException {
    public FileParsingIOException(String message, IOException e) {
        super(message, e);
    }
}