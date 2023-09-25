package edu.uclm.esi.tecsistweb.model.exception.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TySWebErrorImpl {

    private int status_code;
    private HttpStatus status;
    private String path;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    public TySWebErrorImpl(HttpStatus status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.status_code = status.value();
    }

    public TySWebErrorImpl(HttpStatus status, String message, String path, List<String> errors) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.errors = errors;
        this.status_code = status.value();
    }
}
