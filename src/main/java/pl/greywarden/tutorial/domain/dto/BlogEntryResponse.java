package pl.greywarden.tutorial.domain.dto;

public record BlogEntryResponse(Integer id,
                                String author,
                                String content,
                                String publicationDate) {
}
