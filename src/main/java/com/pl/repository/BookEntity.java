package com.pl.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String isbn;

    private String author;

    private String title;

    @ManyToOne
    private LibraryEntity library;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private UserBookEntity userBook;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookEntity that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
