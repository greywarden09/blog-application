package pl.greywarden.tutorial.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BlogEntryNotFoundException extends ResponseStatusException {
    public BlogEntryNotFoundException(Integer entryId) {
        super(HttpStatus.NOT_FOUND, "Blog entry with id %d not found".formatted(entryId));
    }
}
