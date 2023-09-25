package edu.uclm.esi.tecsistweb.model.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class TySWebException extends RuntimeException {

    private HttpStatus status;
    private Throwable cause;
    private String message;

    public TySWebException(HttpStatus status, Throwable cause) {
        this.status = status;
        this.cause = cause;
        this.message = cause.getMessage();
    }
}
