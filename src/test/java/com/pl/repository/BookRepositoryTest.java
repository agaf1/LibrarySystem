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
        //given
        Library library = CreateDataToTests.createLibraryWithBooks("isbn:1", "isbn:2", "isbn:3");
        Library savedLibrary = libraryRepository.save(library);

        //when
        List<Book> booksFromLibrary = bookRepository.findAllBooks(savedLibrary.getId());

        //then
        assertThat(booksFromLibrary).hasSize(3);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_find_all_books_with_given_author() {
        //given
        Library library = CreateDataToTests.createLibraryWithBooks("isbn:1", "isbn:2");
        Book book3 = CreateDataToTests.createBook("isbn:3");
        book3.setAuthor("author");
        library.getBooks().add(book3);
        Library savedLibrary = libraryRepository.save(library);

        String authorOfBook1 = savedLibrary.getBooks().get(0).getAuthor();

        //when
        List<Book> booksWithGivenAuthor = bookRepository.findBooksByAuthor(authorOfBook1);

        //then
        assertThat(booksWithGivenAuthor).hasSize(2);
        assertThat(booksWithGivenAuthor.get(1).getAuthor()).isEqualTo(authorOfBook1);
        assertThat(booksWithGivenAuthor.get(1).getLibrary().getId()).isEqualTo(savedLibrary.getId());
    }

}