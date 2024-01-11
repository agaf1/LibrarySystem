package com.pl.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "libraries")
@Getter
@Setter
@NoArgsConstructor
class LibraryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer number;

    private String address;

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<BookEntity> books = new HashSet<>();

    void addBook(BookEntity... bookEntity) {
        for (var book : bookEntity) {
            books.add(book);
            book.setLibrary(this);
        }
    }

    boolean deleteBook(BookEntity bookEntity) {
        boolean result = books.remove(bookEntity);
        bookEntity.setLibrary(null);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LibraryEntity library)) return false;
        return id != null && Objects.equals(id, library.id);
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
