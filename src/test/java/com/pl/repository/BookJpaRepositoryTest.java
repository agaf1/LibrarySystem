package com.pl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class BookJpaRepositoryTest {

    @Autowired
    private BookJpaRepository bookJpaRepository;
    @Autowired
    private LibraryJpaRepository libraryJpaRepository;

    @Test
    @Sql("clean-db.sql")
    public void should_return_list_of_books_entity_with_given_library_id() {
        LibraryEntity library1 = CreateDataToTests.createLibraryEntity();
        BookEntity book1 = CreateDataToTests.createBookEntity("isbn:1");
        BookEntity book2 = CreateDataToTests.createBookEntity("isbn:2");
        library1.addBook(book1);
        library1.addBook(book2);

        var savedLibrary = libraryJpaRepository.save(library1);

        List<BookEntity> foundBooks = bookJpaRepository.findAllBook(savedLibrary.getId());

        assertThat(foundBooks).hasSize(2);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_return_list_of_books_entity_with_given_author() {
        BookEntity book1 = CreateDataToTests.createBookEntity("isbn:1");
        BookEntity book2 = CreateDataToTests.createBookEntity("isbn:2");
        BookEntity book3 = CreateDataToTests.createBookEntity("isbn:3");
        book3.setAuthor("Alan");

        bookJpaRepository.save(book1);
        bookJpaRepository.save(book2);
        bookJpaRepository.save(book3);

        List<BookEntity> books = bookJpaRepository.findBooksByAuthor("Steve Mann");

        assertThat(books).hasSize(2);
        assertThat(books.get(1).getAuthor()).isEqualTo("Steve Mann");
    }

}