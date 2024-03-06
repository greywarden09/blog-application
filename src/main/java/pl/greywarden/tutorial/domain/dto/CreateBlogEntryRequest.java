package pl.greywarden.tutorial.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import pl.greywarden.tutorial.domain.validation.ValidAuthorId;

public record CreateBlogEntryRequest(
        @ValidAuthorId Integer authorId,
        @NotEmpty(message = "Blog entry must not be empty") String content) {
}
