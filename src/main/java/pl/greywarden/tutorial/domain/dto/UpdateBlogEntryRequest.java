package pl.greywarden.tutorial.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateBlogEntryRequest(@NotNull(message = "Blog entry must not be null")
                                     @NotEmpty(message = "Blog entry must not be empty")
                                     @NotBlank(message = "Blog entry must not be blank") String content) {
}
