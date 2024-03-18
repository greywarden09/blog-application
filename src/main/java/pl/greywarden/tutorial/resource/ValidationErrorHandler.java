package pl.greywarden.tutorial.resource;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.greywarden.tutorial.domain.dto.ErrorMessage;

import java.util.stream.Collectors;

@RestControllerAdvice
class ValidationErrorHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorMessage handleMethodArgumentNotValidException(BindingResult bindingResult) {
        var message = bindingResult
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ErrorMessage(400, message);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorMessage handlePropertyReferenceException(PropertyReferenceException exception) {
        return new ErrorMessage(400, exception.getMessage());
    }
}
