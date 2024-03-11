package pl.greywarden.tutorial.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import pl.greywarden.tutorial.domain.dto.AuthorResponse;
import pl.greywarden.tutorial.domain.dto.CreateAuthorRequest;
import pl.greywarden.tutorial.domain.dto.UpdateAuthorRequest;
import pl.greywarden.tutorial.domain.entity.Author;
import pl.greywarden.tutorial.domain.exceptions.AuthorNotFoundException;
import pl.greywarden.tutorial.repository.AuthorsRepository;

import java.util.List;
import java.util.logging.Logger;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorsService {
    private final AuthorsRepository authorsRepository;
    private final HttpServletResponse servletResponse;

    public void createAuthor(CreateAuthorRequest createAuthorRequest) {
        var author = new Author();
        author.setName(createAuthorRequest.name());
        var saved = authorsRepository.save(author);
        servletResponse.setHeader(HttpHeaders.LOCATION, "/api/authors/" + saved.getId());
    }

    public Page<AuthorResponse> getAllAuthors(Pageable pageable) {
        return authorsRepository.findAll(pageable).map(this::entityToResponse);
    }

    public AuthorResponse getAuthorById(Integer authorId) {
        return authorsRepository.findById(authorId)
                .map(this::entityToResponse)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));
    }

    public void deleteAuthorById(Integer id) {
        authorsRepository.deleteById(id);
    }

    @Transactional
    public void updateAuthorById(Integer id, UpdateAuthorRequest updateAuthorRequest) {
        var author = authorsRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        var newName = updateAuthorRequest.name();

        author.setName(newName);
    }

    private AuthorResponse entityToResponse(Author author) {
        var blogEntries = authorsRepository.getBlogEntries(author)
                .stream()
                .map(entryId -> "/api/blog-entries/" + entryId)
                .toList();
        return new AuthorResponse(author.getId(), author.getName(), blogEntries);
    }
}
