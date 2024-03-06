package pl.greywarden.tutorial.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Service;
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

    public Author createAuthor(CreateAuthorRequest createAuthorRequest) {
        var author = new Author();
        author.setName(createAuthorRequest.name());
        return authorsRepository.save(author);
    }

    public List<Author> getAllAuthors() {
        return authorsRepository.findAll();
    }

    public Author getAuthorById(Integer authorId) {
        return authorsRepository.findById(authorId).orElseThrow();
    }

    public void deleteAuthorById(Integer id) {
        authorsRepository.deleteById(id);
    }

    @Transactional
    public void updateAuthorById(Integer id, UpdateAuthorRequest updateAuthorRequest) {
        var author = authorsRepository.getReferenceById(id);
        var newName = updateAuthorRequest.name();

        author.setName(newName);
    }
}
