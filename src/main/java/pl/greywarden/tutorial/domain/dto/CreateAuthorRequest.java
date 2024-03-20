package pl.greywarden.tutorial.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAuthorRequest(
        @NotNull(message = "Author's name must not be null")
        @NotEmpty(message = "Author's name must not be empty")
        @NotBlank(message = "Author's name must not be blank")
        @Size(max = 250) String name) {
}
