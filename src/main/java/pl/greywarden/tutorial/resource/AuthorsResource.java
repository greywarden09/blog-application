package pl.greywarden.tutorial.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.greywarden.tutorial.domain.dto.CreateAuthorRequest;
import pl.greywarden.tutorial.domain.dto.ErrorMessage;
import pl.greywarden.tutorial.domain.dto.UpdateAuthorRequest;
import pl.greywarden.tutorial.domain.entity.Author;
import pl.greywarden.tutorial.domain.validation.ValidAuthorId;
import pl.greywarden.tutorial.service.AuthorsService;

import java.util.List;

@RestController
@Validated
@RequestMapping(value = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
class AuthorsResource {
    private final AuthorsService authorsService;

    @GetMapping
    @Operation(summary = "Retrieve all Authors")
    @ApiResponses({
            @ApiResponse(
                    description = "List of Authors",
                    responseCode = "200",
                    content = @Content(
                            examples = {
                                    @ExampleObject(name = "List of Authors", value = "[{\"id\": 1, \"name\": \"John Doe\"},{\"id\": 2, \"name\": \"Tony Stark\"}]"),
                                    @ExampleObject(name = "Empty list", value = "[]", description = "Empty list if there are no Authors in database"),
                            }
                    )
            )
    })
    List<Author> getAllAuthors() {
        return authorsService.getAllAuthors();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve an Author by ID")
    @ApiResponses({
            @ApiResponse(
                    description = "Author with specified ID",
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = Author.class),
                            examples = {
                                    @ExampleObject(value = "{\"id\": 1, \"name\": \"John Doe\"}")
                            }
                    )
            ),
            @ApiResponse(
                    description = "Author with specified ID not found",
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = ErrorMessage.class),
                            examples = {
                                    @ExampleObject(value = "{\"statusCode\": 404, \"message\" : \"Author with id not found\"}")
                            }
                    )
            )
    })
    Author getAuthorById(@PathVariable("id") @ValidAuthorId Integer authorId) {
        return authorsService.getAuthorById(authorId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an Author with specific name")
    @ApiResponses({
            @ApiResponse(
                    description = "201 CREATED if resource was created",
                    responseCode = "201",
                    headers = {
                            @Header(name = HttpHeaders.LOCATION, description = "Location of new created Author")
                    }
            ),
            @ApiResponse(
                    description = "Error if name is null, blank or empty",
                    responseCode = "400",
                    content = @Content(
                            schema = @Schema(implementation = ErrorMessage.class),
                            examples = {
                                    @ExampleObject(value = "{\"statusCode\": 400, \"message\" : \"Author's name must not be empty\"}")
                            }
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Create Author request",
            content = @Content(
                    examples = {
                            @ExampleObject(value = "{\"name\": \"John Doe\"}")
                    }
            )
    )
    ResponseEntity<?> createAuthor(@RequestBody CreateAuthorRequest createAuthorRequest) {
        var author = authorsService.createAuthor(createAuthorRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, "/api/authors/" + author.getId())
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove an Author by ID")
    @ApiResponse(
            description = "No content will be returned even if Author does not exist"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAuthorById(@PathVariable("id") Integer id) {
        authorsService.deleteAuthorById(id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an Author by ID")
    @ApiResponses({
            @ApiResponse(
                    description = "No content if everything is fine",
                    responseCode = "202"
            ),
            @ApiResponse(
                    description = "404 NOT FOUND if Author with ID doesn't exist",
                    responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = ErrorMessage.class),
                            examples = {
                                    @ExampleObject(value = "{\"statusCode\": 404, \"message\" : \"Author with id 5 not found\"}")
                            }
                    )
            ),
            @ApiResponse(
                    description = "Error if name is null, blank or empty",
                    responseCode = "400",
                    content = @Content(
                            schema = @Schema(implementation = ErrorMessage.class),
                            examples = {
                                    @ExampleObject(value = "{\"statusCode\": 400, \"message\" : \"Author's name must not be empty\"}")
                            }
                    )
            )
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    void updateAuthorById(@RequestBody UpdateAuthorRequest updateAuthorRequest,
                          @PathVariable("id") @ValidAuthorId Integer id) {
        authorsService.updateAuthorById(id, updateAuthorRequest);
    }
}
