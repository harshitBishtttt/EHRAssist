package EHRAssist.exceptionHandler;

import EHRAssist.dto.response.ErrorResponse;
import EHRAssist.exceptionHandler.exceptions.FhirBadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Internal Server Error"));
    }

    @ExceptionHandler(FhirBadRequestException.class)
    public ResponseEntity<String> handleBadRequest(FhirBadRequestException ex) {

        String operationOutcome = """
                {
                  "resourceType": "OperationOutcome",
                  "issue": [
                    {
                      "severity": "error",
                      "code": "required",
                      "details": {
                        "text": "%s"
                      }
                    }
                  ]
                }
                """.formatted(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(operationOutcome);
    }

}
