package EHRAssist.exceptionHandler.exceptions;

public class InvalidFirebaseCredException extends RuntimeException {
    public InvalidFirebaseCredException(String msg) {
        super(msg);
    }
}
