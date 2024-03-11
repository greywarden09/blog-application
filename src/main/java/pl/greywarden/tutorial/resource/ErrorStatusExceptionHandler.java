package pl.greywarden.tutorial.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import pl.greywarden.tutorial.domain.dto.ErrorMessage;
import pl.greywarden.tutorial.domain.exception.AuthorNotFoundException;
import pl.greywarden.tutorial.domain.exception.BlogEntryNotFoundException;

@RestControllerAdvice
class ErrorStatusExceptionHandler {
    @ExceptionHandler({AuthorNotFoundException.class, BlogEntryNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorMessage handleException(ResponseStatusException exception) {
        return new ErrorMessage(exception.getStatusCode().value(), exception.getReason());
    }
}
