package pl.greywarden.tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.greywarden.tutorial.domain.entity.Author;

import java.util.List;

public interface AuthorsRepository extends JpaRepository<Author, Integer> {
    @Query("SELECT b.id FROM BlogEntry b WHERE b.author = :author")
    List<Integer> getBlogEntries(Author author);
}
