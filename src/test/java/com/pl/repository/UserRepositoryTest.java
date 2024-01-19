package com.pl.repository;

import com.pl.service.domain.Library;
import com.pl.service.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql("clean-db.sql")
    public void should_save_user() {
        //given
        User expectedUser = CreateDataToTests.createUser();

        // when
        User savedUser = userRepository.save(expectedUser);

        //then
        Optional<User> actualUser = userRepository.findById(savedUser.getId());
        assertThat(actualUser.get()).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedUser);

        assertThat(savedUser).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedUser);

        assertThat(actualUser.get().getId()).isNotNull();
    }

    @Test
    @Sql("clean-db.sql")
    public void should_borrowed_book() {
        //given
        User user = CreateDataToTests.createUser();
        User savedUser = userRepository.save(user);

        Library library = CreateDataToTests.createLibraryWithBooks("isbn:1", "isbn:2");
        Library savedLibrary = libraryRepository.save(library);

        Integer userId = savedUser.getId();
        Integer bookId = savedLibrary.getBooks().stream().findFirst().get().getId();

        //when
        boolean result = userRepository.borrowBook(userId, bookId);

        //then
        assertThat(result).isEqualTo(true);

        User userWithBorrowedBook = userRepository.findById(userId).get();
        assertThat(userWithBorrowedBook.getBorrowedBooks()).hasSize(1);
        assertThat(userWithBorrowedBook.getBorrowedBooks().get(0).getId()).isEqualTo(bookId);
        assertThat(userWithBorrowedBook.getBorrowedBooks().get(0).getBorrowingDate())
                .isEqualTo(LocalDate.now());

    }

    @Test
    @Sql("clean-db.sql")
    public void should_return_book() {
        //given
        User user = CreateDataToTests.createUser();
        User savedUser = userRepository.save(user);

        Library library = CreateDataToTests.createLibraryWithBooks("isbn:1", "isbn:2");
        Library savedLibrary = libraryRepository.save(library);

        Integer userId = savedUser.getId();
        Integer bookId = savedLibrary.getBooks().stream().findFirst().get().getId();

        userRepository.borrowBook(userId, bookId);

        //when
        boolean result = userRepository.returnBook(bookId);

        //then
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(result).isEqualTo(true);
        assertThat(foundUser.get().getBorrowedBooks()).hasSize(0);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_extend_date_of_borrow_book() {
        //given
        User user = CreateDataToTests.createUser();
        User savedUser = userRepository.save(user);

        Library library = CreateDataToTests.createLibraryWithBooks("isbn:1", "isbn:2");
        Library savedLibrary = libraryRepository.save(library);

        Integer userId = savedUser.getId();
        Integer bookId = savedLibrary.getBooks().stream().findFirst().get().getId();

        userRepository.borrowBook(userId, bookId);

        //when
        LocalDate result = userRepository.extendTimeOfBorrowBook(bookId);

        //then
        LocalDate actualBorrowDate = bookRepository.findById(bookId).get().getBorrowingDate();

        assertThat(result).isEqualTo(actualBorrowDate.plusMonths(1));
    }

    @Test
    @Sql("clean-db.sql")
    public void should_not_extend_date_of_borrow_book_and_return_today_date() {
        //given
        User user = CreateDataToTests.createUser();
        User savedUser = userRepository.save(user);

        Library library = CreateDataToTests.createLibraryWithBooks("isbn:1", "isbn:2");
        Library savedLibrary = libraryRepository.save(library);

        Integer bookId = 3;

        //when
        LocalDate result = userRepository.extendTimeOfBorrowBook(bookId);

        //then
        assertThat(result).isEqualTo(LocalDate.now());
    }

}