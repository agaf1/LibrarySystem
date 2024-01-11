package com.pl.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LibraryJpaRepositoryTest {

    @Autowired
    private LibraryJpaRepository libraryJpa;
    @Autowired
    private BookJpaRepository bookJpa;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    @Sql("clean-db.sql")
    public void should_save_new_library() {
        LibraryEntity library1 = CreateDataToTests.createLibraryEntity();

        LibraryEntity savedLibrary = libraryJpa.save(library1);

        LibraryEntity actualLibrary = libraryJpa.findById(savedLibrary.getId()).get();

        assertThat(actualLibrary).isEqualTo(savedLibrary);
        assertThat(actualLibrary.getAddress()).isEqualTo(library1.getAddress());
    }

    @Test
    @Sql("clean-db.sql")
    public void should_create_new_library_with_book() {
        LibraryEntity library = CreateDataToTests.createLibraryEntity();
        BookEntity book1 = CreateDataToTests.createBookEntity("ISBN:45621236544");
        BookEntity book2 = CreateDataToTests.createBookEntity("ISBN:45621236545");
        BookEntity book3 = CreateDataToTests.createBookEntity("ISBN:45621236546");

        library.addBook(book1, book2, book3);

        LibraryEntity savedLibrary = libraryJpa.save(library);

        LibraryEntity actualLibrary = libraryJpa.findById(savedLibrary.getId()).get();

        assertThat(actualLibrary).isEqualTo(savedLibrary);
        assertThat(actualLibrary.getBooks()).hasSize(3);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_add_book_to_existing_library() {
        LibraryEntity library1 = CreateDataToTests.createLibraryEntity();
        library1.addBook(CreateDataToTests.createBookEntity("isbn1"));
        var savedLibrary = libraryJpa.save(library1);
        var bookToAdd = CreateDataToTests.createBookEntity("ISBN:564998766245");

        transactionTemplate.execute(t -> {
            LibraryEntity libraryFromDB = libraryJpa.findById(savedLibrary.getId()).get();
            libraryFromDB.addBook(bookToAdd);
            return libraryJpa.save(libraryFromDB);
        });

        LibraryEntity libraryFromDB = libraryJpa.findById(savedLibrary.getId()).get();

        assertThat(libraryFromDB.getBooks()).hasSize(2);
        assertThat(libraryFromDB.getBooks()).anyMatch(book -> book.getIsbn().equals(bookToAdd.getIsbn()));
    }

    @Test
    @Sql("clean-db.sql")
    public void should_delete_book_from_library() {
        LibraryEntity newLibrary = CreateDataToTests.createLibraryEntity();
        BookEntity book1 = CreateDataToTests.createBookEntity("isbn:1");
        BookEntity book2 = CreateDataToTests.createBookEntity("isbn:2");
        BookEntity book3 = CreateDataToTests.createBookEntity("isbn:3");
        newLibrary.addBook(book1, book2, book3);


        var savedLibrary = transactionTemplate.execute(t -> {
            return libraryJpa.save(newLibrary);
        });

        BookEntity bookToRemove = savedLibrary.getBooks().stream().findFirst().get();

        transactionTemplate.execute(t -> {
            var library = libraryJpa.findById(savedLibrary.getId()).get();
            var book = library.getBooks().stream().filter(b -> b.equals(bookToRemove)).findAny().get();
            library.deleteBook(book);
            return null;
        });

        var storedLibrary = libraryJpa.findById(savedLibrary.getId()).get();
        assertThat(storedLibrary.getBooks().size()).isEqualTo(2);

        assertThat(storedLibrary.getBooks()).doesNotContain(bookToRemove);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_add_existing_book_to_existing_library() {
        LibraryEntity library1 = CreateDataToTests.createLibraryEntity();
        BookEntity book1 = CreateDataToTests.createBookEntity("isbn:1");
        BookEntity book2 = CreateDataToTests.createBookEntity("isbn:2");
        library1.addBook(book1);
        library1.addBook(book2);

        var savedLibrary = libraryJpa.save(library1);

        BookEntity book3 = CreateDataToTests.createBookEntity("isbn:3");
        BookEntity existingBook3 = bookJpa.save(book3);

        Assertions.assertEquals(null, existingBook3.getLibrary());

        savedLibrary.addBook(existingBook3);
        var libraryWithAddedBook3 = libraryJpa.save(savedLibrary);

        Assertions.assertEquals(libraryWithAddedBook3, existingBook3.getLibrary());
        Assertions.assertEquals(3, libraryWithAddedBook3.getBooks().size());
    }
}