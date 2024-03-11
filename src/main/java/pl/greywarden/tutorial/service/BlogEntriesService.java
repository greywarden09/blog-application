package pl.greywarden.tutorial.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import pl.greywarden.tutorial.domain.dto.BlogEntryResponse;
import pl.greywarden.tutorial.domain.dto.CreateBlogEntryRequest;
import pl.greywarden.tutorial.domain.dto.UpdateBlogEntryRequest;
import pl.greywarden.tutorial.domain.entity.BlogEntry;
import pl.greywarden.tutorial.domain.exception.BlogEntryNotFoundException;
import pl.greywarden.tutorial.repository.AuthorsRepository;
import pl.greywarden.tutorial.repository.BlogEntriesRepository;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class BlogEntriesService {
    private final BlogEntriesRepository blogEntriesRepository;
    private final AuthorsRepository authorsRepository;
    private final HttpServletResponse servletResponse;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Page<BlogEntryResponse> getAllBlogEntries(Pageable pageable) {
        return blogEntriesRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    private BlogEntryResponse convertToResponse(BlogEntry blogEntry) {
        var entryId = blogEntry.getId();
        var author = blogEntry.getAuthor().getName();
        var content = blogEntry.getContent();
        var publicationDate = formatter.format(blogEntry.getPublicationDate());

        return new BlogEntryResponse(entryId, author, content, publicationDate);
    }

    public void createBlogEntry(CreateBlogEntryRequest createBlogEntryRequest) {
        var authorId = createBlogEntryRequest.authorId();
        var author = authorsRepository.getReferenceById(authorId);

        var content = createBlogEntryRequest.content();

        var blogEntry = new BlogEntry();
        blogEntry.setContent(content);
        blogEntry.setAuthor(author);

        var saved = blogEntriesRepository.save(blogEntry);

        servletResponse.setHeader(HttpHeaders.LOCATION, "/api/blog-entries/" + saved.getId());
    }

    public void deleteBlogEntryById(Integer blogEntryId) {
        blogEntriesRepository.deleteById(blogEntryId);
    }

    public BlogEntryResponse getBlogEntryById(Integer blogEntryId) {
        var blogEntry = blogEntriesRepository.findById(blogEntryId)
                .orElseThrow(() -> new BlogEntryNotFoundException(blogEntryId));

        var entryId = blogEntry.getId();
        var author = blogEntry.getAuthor().getName();
        var content = blogEntry.getContent();
        var publicationDate = formatter.format(blogEntry.getPublicationDate());

        return new BlogEntryResponse(entryId, author, content, publicationDate);
    }

    @Transactional
    public void updateBlogEntry(Integer blogEntryId, UpdateBlogEntryRequest updateBlogEntryRequest) {
        var blogEntry = blogEntriesRepository.findById(blogEntryId)
                .orElseThrow(() -> new BlogEntryNotFoundException(blogEntryId));

        blogEntry.setContent(updateBlogEntryRequest.content());
    }
}
