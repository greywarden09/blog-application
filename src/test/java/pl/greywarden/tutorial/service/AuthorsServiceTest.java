package pl.greywarden.tutorial.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import jakarta.servlet.http.HttpServletResponse;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import pl.greywarden.tutorial.domain.dto.AuthorResponse;
import pl.greywarden.tutorial.domain.dto.CreateAuthorRequest;
import pl.greywarden.tutorial.domain.entity.Author;
import pl.greywarden.tutorial.domain.exception.AuthorNotFoundException;
import pl.greywarden.tutorial.domain.mapper.AuthorMapper;
import pl.greywarden.tutorial.repository.AuthorsRepository;

import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorsServiceTest {
    private ListAppender<ILoggingEvent> logWatcher;

    private static final Faker faker = new Faker();

    @Mock
    private AuthorsRepository authorsRepository;

    @Mock
    private HttpServletResponse servletResponse;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorsService authorsService;

    @BeforeEach
    void setUp() {
        logWatcher = new ListAppender<>();
        logWatcher.start();
        var logger = (Logger)LoggerFactory.getLogger(AuthorsService.class);
        logger.addAppender(logWatcher);
    }

    @Test
    void shouldCreateAuthor() {
        // given
        var author = mock(Author.class);
        var authorsName = faker.name().name();
        var createAuthorRequest = new CreateAuthorRequest(authorsName);

        when(author.getId()).thenReturn(42);
        when(authorMapper.toEntity(createAuthorRequest)).thenReturn(author);
        when(authorsRepository.save(author)).thenReturn(author);

        // when
        authorsService.createAuthor(createAuthorRequest);

        // then
        verify(servletResponse, times(1)).setHeader(HttpHeaders.LOCATION, "/api/authors/42");
        verify(authorsRepository, times(1)).save(author);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldGetAllAuthors() {
        // given
        var pageable = mock(Pageable.class);
        Page<Author> page = mock(Page.class);

        when(authorsRepository.findAll(pageable)).thenReturn(page);

        // when
        authorsService.getAllAuthors(pageable);

        // then
        verify(authorsRepository, times(1)).findAll(pageable);
        verify(page, times(1)).map(any());
    }

    @Test
    void shouldGetAuthorById() {
        // given
        var author = mock(Author.class);
        var response = mock(AuthorResponse.class);
        var authorId = 42;

        when(authorsRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorMapper.toResponse(author)).thenReturn(response);

        // when
        authorsService.getAuthorById(authorId);

        // then
        verify(authorsRepository, times(1)).findById(authorId);
        verify(authorMapper, times(1)).toResponse(author);
    }

    @Test
    void shouldThrowAuthorNotFoundExceptionOnInvalidAuthorsId() {
        // expect
        assertThrows(AuthorNotFoundException.class, () -> authorsService.getAuthorById(42));
    }

    @Test
    void shouldDeleteAuthorById() {
        // given
        var authorId = 42;

        // when
        authorsService.deleteAuthorById(authorId);

        // then
        verify(authorsRepository, times(1)).deleteById(authorId);
    }

    @Test
    void shouldLogErrorMessageOnFailedCSVImport() throws Exception {
        // given
        var csv = mock(MultipartFile.class);

        when(csv.getInputStream()).thenThrow(new IOException("test exception"));

        // when
        authorsService.populateWithCSV(csv);
        var logMessage = logWatcher.list.get(0);

        // then
        assertEquals(Level.ERROR, logMessage.getLevel());
        assertThat(logMessage.getMessage(), not(emptyOrNullString()));
    }
}
