package pl.greywarden.tutorial.domain.dto;

import java.util.List;

public record AuthorResponse(Integer id, String name, List<String> blogEntries) {
}
