package com.jk.WeatherAPI.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class RestAPIAdvice {
    private final Logger log = LoggerFactory.getLogger(RestAPIAdvice.class);

    @ExceptionHandler(AppException.class)
    public ResponseEntity<JsonError> handleAppException(final HttpServletRequest req, final AppException ex) {
        log.error("Application Exception: status={}, reason={}, ", ex.getHttpStatus(), ex.getErrorMessage(), ex);

        JsonError jsonError = new JsonError(
                new Date(),
                ex.getHttpStatus().value(),
                ex.getHttpStatus().getReasonPhrase(),
                ex.getMessage(),
                req.getServletPath()
        );
        return ResponseEntity.status(ex.getHttpStatus()).body(jsonError);
    }

    public class JsonError {
        public final Date timestamp;
        public final int status;
        public final String error;
        public final String message;
        public final String path;

        public JsonError(final Date timestamp, final int status, final String error, final String message, final String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }
    }
}

