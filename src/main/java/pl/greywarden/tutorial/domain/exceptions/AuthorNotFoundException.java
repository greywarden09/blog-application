package pl.greywarden.tutorial.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuthorNotFoundException extends ResponseStatusException {
    public AuthorNotFoundException(Integer authorId) {
        super(HttpStatus.NOT_FOUND, "Author with id %d not found".formatted(authorId));
    }
}
