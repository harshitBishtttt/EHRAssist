package EHRAssist.exceptionHandler;

import EHRAssist.dto.response.ErrorResponse;
import EHRAssist.exceptionHandler.exceptions.InvalidFirebaseCredException;
import EHRAssist.exceptionHandler.exceptions.MissingParametersException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception ex) {
        // Log the exception with stack trace
        logger.error("Internal server error occurred", ex);

        // Return clean error response to client
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Internal Server Error"));
    }

    @ExceptionHandler(InvalidFirebaseCredException.class)
    public ResponseEntity<ErrorResponse> handelInvalidFirebaseCredException(InvalidFirebaseCredException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MissingParametersException.class)
    public ResponseEntity<ErrorResponse> handelMissingParametersException(MissingParametersException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }


}
