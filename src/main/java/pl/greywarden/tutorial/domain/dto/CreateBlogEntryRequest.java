package pl.greywarden.tutorial.domain.dto;

import pl.greywarden.tutorial.domain.validation.ValidAuthorId;

public record CreateBlogEntryRequest(
        @ValidAuthorId Integer authorId) {
}
