package pl.greywarden.tutorial.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.greywarden.tutorial.domain.dto.BlogEntryResponse;
import pl.greywarden.tutorial.domain.dto.CreateBlogEntryRequest;
import pl.greywarden.tutorial.domain.dto.UpdateBlogEntryRequest;
import pl.greywarden.tutorial.service.BlogEntriesService;

@RestController
@RequestMapping(value = "/blog-entries", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
class BlogEntriesResource {
    private final BlogEntriesService blogEntriesService;

    @GetMapping
    Page<BlogEntryResponse> getAllBlogEntries(@ParameterObject Pageable pageable) {
        return blogEntriesService.getAllBlogEntries(pageable);
    }

    @GetMapping("/{id}")
    BlogEntryResponse getBlogEntryById(@PathVariable("id") Integer blogEntryId) {
        return blogEntriesService.getBlogEntryById(blogEntryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createBlogEntry(@RequestBody @Valid CreateBlogEntryRequest createBlogEntryRequest) {
        blogEntriesService.createBlogEntry(createBlogEntryRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteBlogEntry(@PathVariable("id") Integer blogEntryId) {
        blogEntriesService.deleteBlogEntryById(blogEntryId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void updateBlogEntry(@PathVariable("id") Integer blogEntryId,
                         @RequestBody @Valid UpdateBlogEntryRequest updateBlogEntryRequest) {
        blogEntriesService.updateBlogEntry(blogEntryId, updateBlogEntryRequest);
    }
}
