package EHRAssist.exceptionHandler.exceptions;

public class FhirBadRequestException extends RuntimeException {
    public FhirBadRequestException(String msg) {
        super(msg);
    }
}
