package pl.greywarden.tutorial.domain.mapper;

import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import pl.greywarden.tutorial.domain.dto.CreateBlogEntryRequest;
import pl.greywarden.tutorial.domain.entity.Author;
import pl.greywarden.tutorial.domain.entity.BlogEntry;
import pl.greywarden.tutorial.repository.AuthorsRepository;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class BlogEntryMapper {
    @Setter(onMethod = @__(@Autowired))
    private AuthorsRepository authorsRepository;

    @Mappings({
            @Mapping(source = "content", target = "content"),
            @Mapping(source = "createBlogEntryRequest", target = "author", qualifiedByName = "author"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "publicationDate", ignore = true)
    })
    public abstract BlogEntry toEntity(CreateBlogEntryRequest createBlogEntryRequest);

    @Named("author")
    public Author getAuthorFromRequest(CreateBlogEntryRequest createBlogEntryRequest) {
        return authorsRepository.getReferenceById(createBlogEntryRequest.authorId());
    }
}
