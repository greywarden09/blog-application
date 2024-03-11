package pl.greywarden.tutorial.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.greywarden.tutorial.domain.dto.ErrorMessage;
import pl.greywarden.tutorial.domain.exceptions.AuthorNotFoundException;

@RestControllerAdvice
class ErrorStatusExceptionHandler {
    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorMessage handleException(AuthorNotFoundException exception) {
        var message = exception.getReason();

        return new ErrorMessage(exception.getStatusCode().value(), message);
    }
}
