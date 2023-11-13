package edu.uclm.esi.tecsistweb.config;


import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.model.exception.model.TySWebErrorImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.sql.SQLException;


@ControllerAdvice
@EnableWebMvc
public class RestResponseEntityHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER_ERROR = (Logger) LoggerFactory.getLogger("TySWeb_Exception_Handler");


    private final ResponseEntity<Object> buildTySWebException(String message, WebRequest request, HttpStatus status) {
        TySWebErrorImpl error = new TySWebErrorImpl(status, message, ((ServletWebRequest) request).getRequest().getRequestURI());
        LOGGER_ERROR.error(message);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(TySWebException.class)
    public ResponseEntity<Object> handleTySWeb_Exception(TySWebException exception, WebRequest webRequest, HttpServletRequest request) {

        String error = exception.getMessage();
        if (exception != null && exception.getLocalizedMessage() != null && StringUtils.isNotBlank(exception.getLocalizedMessage())) {
            error = exception.getLocalizedMessage();
        }
        return buildTySWebException(error, webRequest, exception.getStatus());
    }


    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handle(Exception ex, HttpServletRequest request, WebRequest webRequest) {
        return buildTySWebException(ex.getMessage(), webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({SQLException.class})
    public ResponseEntity<?> handleSQLException(SQLException ex, HttpServletRequest request, WebRequest webRequest) {
        LOGGER_ERROR.error("Error executing mysql query: " + ex.getMessage());
        return buildTySWebException("Error executing request", webRequest, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}