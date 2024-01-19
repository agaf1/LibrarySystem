package com.pl.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<UserBookEntity> borrowedBooks = new HashSet<>();


    public void borrowBook(BookEntity book) {
        UserBookEntity userBookEntity = new UserBookEntity();
        userBookEntity.setUser(this);
        userBookEntity.setBook(book);
        userBookEntity.setBorrowingDate(LocalDate.now());

        borrowedBooks.add(userBookEntity);
        book.setUserBook(userBookEntity);
    }

    public UserBookEntity returnBook(BookEntity book) {
        UserBookEntity userBook = borrowedBooks.stream()
                .filter(userBookEntity -> userBookEntity.getBook().equals(book))
                .findFirst()
                .get();

        userBook.setUser(null);
        userBook.setBook(null);
        book.setUserBook(null);
        borrowedBooks.remove(book);

        return userBook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
