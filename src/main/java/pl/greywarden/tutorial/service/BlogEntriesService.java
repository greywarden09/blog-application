package pl.greywarden.tutorial.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.greywarden.tutorial.domain.dto.BlogEntryResponse;
import pl.greywarden.tutorial.domain.entity.BlogEntry;
import pl.greywarden.tutorial.repository.BlogEntriesRepository;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class BlogEntriesService {
    private final BlogEntriesRepository blogEntriesRepository;

    public Page<BlogEntryResponse> getAllBlogEntries(int pageNumber, int pageSize) {
        var pageRequest = PageRequest.of(pageNumber, pageSize);

        return blogEntriesRepository.findAll(pageRequest)
                .map(this::convertToResponse);
    }

    private BlogEntryResponse convertToResponse(BlogEntry blogEntry) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        var entryId = blogEntry.getId();
        var author = blogEntry.getAuthor().getName();
        var content = blogEntry.getContent();
        var publicationDate = formatter.format(blogEntry.getPublicationDate());

        return new BlogEntryResponse(entryId, author, content, publicationDate);
    }
}
