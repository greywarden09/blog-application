package pl.greywarden.tutorial;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.greywarden.tutorial.domain.entity.Author;
import pl.greywarden.tutorial.domain.entity.BlogEntry;
import pl.greywarden.tutorial.repository.AuthorsRepository;
import pl.greywarden.tutorial.repository.BlogEntriesRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Profile("dev")
@Component
@RequiredArgsConstructor
class DevDataInitializer implements InitializingBean {
    private final AuthorsRepository authorsRepository;
    private final BlogEntriesRepository blogEntriesRepository;

    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    private static final Lorem lorem = LoremIpsum.getInstance();

    @Override
    public void afterPropertiesSet() {
        var authors = Instancio.ofList(Author.class)
                .size(10)
                .supply(Select.field(Author::getName), this::generateAuthorName)
                .ignore(Select.field(Author::getId))
                .create();
        authorsRepository.saveAll(authors);

        var blogEntries = Instancio.ofList(BlogEntry.class)
                .size(100)
                .supply(Select.field(BlogEntry::getContent), this::generateBlogEntryContent)
                .supply(Select.field(BlogEntry::getAuthor), () -> pickRandomAuthor(authors))
                .supply(Select.field(BlogEntry::getPublicationDate), this::generatePublicationDate)
                .ignore(Select.field(BlogEntry::getId))
                .create();

        blogEntriesRepository.saveAll(blogEntries);
    }

    private String generateAuthorName() {
        return faker.name().fullName();
    }

    private String generateBlogEntryContent() {
        return lorem.getWords(10, 256);
    }

    private Author pickRandomAuthor(List<Author> authors) {
        var index = random.nextInt(authors.size());
        return authors.get(index);
    }

    private LocalDateTime generatePublicationDate() {
        var startDate = new Date(Instant.now().minus(Duration.ofDays(5 * 365)).getEpochSecond());
        var endDate = new Date(Instant.now().getEpochSecond());

        var randomDate = faker.date().between(startDate, endDate);
        return LocalDateTime.ofInstant(randomDate.toInstant(), ZoneOffset.systemDefault());
    }
}
