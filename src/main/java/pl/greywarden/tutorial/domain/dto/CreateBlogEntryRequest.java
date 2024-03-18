package pl.greywarden.tutorial.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import pl.greywarden.tutorial.domain.validation.ValidAuthorId;

public record CreateBlogEntryRequest(
        @ValidAuthorId Integer authorId,
        @NotNull(message = "Blog entry must not be null")
        @NotEmpty(message = "Blog entry must not be empty")
        @NotBlank(message = "Blog entry must not be blank") String content) {
}
