package com.pl.repository;

import com.pl.service.domain.Book;
import com.pl.service.domain.Library;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LibraryRepository libraryRepository;

    @Test
    @Sql("clean-db.sql")
    public void should_find_all_books_from_given_library() {
        Library library = CreateDataToTests.createLibrary();
        Book book1 = CreateDataToTests.createBook("isbn:1");
        Book book2 = CreateDataToTests.createBook("isbn:2");
        Book book3 = CreateDataToTests.createBook("isbn:3");
        List<Book> books = List.of(book1, book2, book3);
        library.setBooks(books);
        LibraryEntity savedLibrary = libraryRepository.save(library);

        List<BookEntity> booksFromLibrary = bookRepository.findAllBooks(savedLibrary.getId());

        assertThat(booksFromLibrary).hasSize(3);
    }

}