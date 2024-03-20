package pl.greywarden.tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.greywarden.tutorial.domain.entity.Author;
import pl.greywarden.tutorial.domain.entity.BlogEntry;

import java.util.List;

public interface BlogEntriesRepository extends JpaRepository<BlogEntry, Integer> {
    List<BlogEntry> findByAuthor(Author author);
}
