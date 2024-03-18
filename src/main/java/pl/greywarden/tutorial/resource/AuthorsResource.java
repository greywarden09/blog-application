package pl.greywarden.tutorial.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.greywarden.tutorial.domain.dto.AuthorResponse;
import pl.greywarden.tutorial.domain.dto.CreateAuthorRequest;
import pl.greywarden.tutorial.domain.dto.UpdateAuthorRequest;
import pl.greywarden.tutorial.service.AuthorsService;

@RestController
@RequestMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
class AuthorsResource {
    private final AuthorsService authorsService;

    @GetMapping
    Page<AuthorResponse> getAllAuthors(@ParameterObject Pageable pageable) {
        return authorsService.getAllAuthors(pageable);
    }

    @GetMapping("/{id}")
    AuthorResponse getAuthorById(@PathVariable("id") Integer authorId) {
        return authorsService.getAuthorById(authorId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createAuthor(@RequestBody @Valid CreateAuthorRequest createAuthorRequest) {
        authorsService.createAuthor(createAuthorRequest);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    void populateWithCSV(@RequestPart("file") MultipartFile csv) {
        authorsService.populateWithCSV(csv);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAuthorById(@PathVariable("id") Integer id) {
        authorsService.deleteAuthorById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void updateAuthorById(@RequestBody @Valid UpdateAuthorRequest updateAuthorRequest,
                          @PathVariable("id") Integer id) {
        authorsService.updateAuthorById(id, updateAuthorRequest);
    }
}
