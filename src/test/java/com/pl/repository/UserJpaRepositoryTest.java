package com.pl.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
class UserJpaRepositoryTest {

    @Autowired
    UserJpaRepository userJpa;
    @Autowired
    LibraryJpaRepository libraryJpa;
    @Autowired
    UserBookJpaRepository userBookJpa;

    @Test
    @Sql("clean-db.sql")
    public void should_save_new_user() {
        UserEntity userEntity = createUser();

        var savedUser = userJpa.save(userEntity);

        Assertions.assertNotNull(savedUser.getId());
    }

    @Test
    @Sql("clean-db.sql")
    public void should_add_book_to_list_of_borrowed_book_of_user() {
        UserEntity userEntity = createUser();
        var savedUser = userJpa.save(userEntity);

        LibraryEntity library1 = CreateDataToTests.createLibraryEntity();
        BookEntity book1 = CreateDataToTests.createBookEntity("isbn:1");
        BookEntity book2 = CreateDataToTests.createBookEntity("isbn:2");
        library1.addBook(book1);
        library1.addBook(book2);
        var savedLibrary = libraryJpa.save(library1);

        savedUser.borrowBook(savedLibrary.getBooks().stream().findFirst().get());
        var savedUserWithBook = userJpa.save(savedUser);

        Assertions.assertEquals(1, savedUserWithBook.getBorrowedBooks().size());
    }

    @Test
    @Sql("clean-db.sql")
    public void should_delete_book_from_list_of_borrowed_book_of_user() {
        //given
        UserEntity userEntity = createUser();
        var savedUser = userJpa.save(userEntity);

        LibraryEntity library1 = CreateDataToTests.createLibraryEntity();
        BookEntity book1 = CreateDataToTests.createBookEntity("isbn:1");
        BookEntity book2 = CreateDataToTests.createBookEntity("isbn:2");
        library1.addBook(book1);
        library1.addBook(book2);
        var savedLibrary = libraryJpa.save(library1);

        var bookToBorrow = savedLibrary.getBooks().stream().findFirst().get();
        savedUser.borrowBook(bookToBorrow);
        var savedUserWithBook = userJpa.save(savedUser);

        Assertions.assertEquals(1, userBookJpa.count());

        //when
        UserBookEntity userBook = savedUserWithBook.returnBook(bookToBorrow);
        userJpa.save(savedUserWithBook);

        userBookJpa.delete(userBook);

        //then
        UserEntity actualUser = userJpa.findById(savedUserWithBook.getId()).get();

        Assertions.assertEquals(0, userBookJpa.count());
        Assertions.assertEquals(0, actualUser.getBorrowedBooks().size());
    }

    private static UserEntity createUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Aga");
        userEntity.setLastName("Fifi");
        userEntity.setEmail("aga@fifi.com");
        return userEntity;
    }
}