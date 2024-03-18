package pl.greywarden.tutorial.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "BLOG_ENTRIES")
@EqualsAndHashCode
public class BlogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Author author;
    @Column(length = Integer.MAX_VALUE)
    private String content;
    private LocalDateTime publicationDate = LocalDateTime.now();
}
