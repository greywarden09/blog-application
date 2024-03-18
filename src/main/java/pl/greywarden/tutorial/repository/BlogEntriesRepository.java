package pl.greywarden.tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.greywarden.tutorial.domain.entity.BlogEntry;

public interface BlogEntriesRepository extends JpaRepository<BlogEntry, Integer> {

}
