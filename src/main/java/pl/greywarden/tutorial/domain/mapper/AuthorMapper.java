package pl.greywarden.tutorial.domain.mapper;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import pl.greywarden.tutorial.domain.dto.AuthorResponse;
import pl.greywarden.tutorial.domain.dto.CreateAuthorRequest;
import pl.greywarden.tutorial.domain.entity.Author;
import pl.greywarden.tutorial.domain.entity.BlogEntry;
import pl.greywarden.tutorial.repository.BlogEntriesRepository;

import java.util.List;

@RequiredArgsConstructor
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class AuthorMapper {
    @Setter(onMethod = @__(@Autowired))
    private BlogEntriesRepository blogEntriesRepository;

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(target = "id", ignore = true)
    })
    public abstract Author toEntity(CreateAuthorRequest createAuthorRequest);

    @Mapping(source = "author", target = "blogEntries", qualifiedByName = "blogEntries")
    public abstract AuthorResponse toResponse(Author author);

    @Named("blogEntries")
    public List<String> blogEntries(Author author) {
        return blogEntriesRepository.findByAuthor(author)
                .stream()
                .map(BlogEntry::getId)
                .map(entryId -> "/api/blog-entries/" + entryId)
                .toList();
    }
}
