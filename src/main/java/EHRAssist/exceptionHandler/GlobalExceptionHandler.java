package EHRAssist.exceptionHandler;

import EHRAssist.dto.response.ErrorResponse;
import EHRAssist.exceptionHandler.exceptions.InvalidFirebaseCredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidFirebaseCredException.class)
    public ResponseEntity<ErrorResponse> handelInvalidFirebaseCredException(InvalidFirebaseCredException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ex.getMessage()));
    }

}
