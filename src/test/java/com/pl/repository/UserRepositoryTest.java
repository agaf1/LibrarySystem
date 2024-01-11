package com.pl.repository;

import com.pl.service.domain.Book;
import com.pl.service.domain.Library;
import com.pl.service.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserBookJpaRepository userBookJpaRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = new UserRepository(userJpaRepository, userMapper, bookRepository, userBookJpaRepository);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_save_user_and_return_user_entity() {
        User user = CreateDataToTests.createUser();

        UserEntity savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getFirstName()).isEqualTo("Ala");
        assertThat(savedUser.getLastName()).isEqualTo("Alutka");
        assertThat(savedUser.getEmail()).isEqualTo("ala@alutka.com");
    }

    @Test
    @Sql("clean-db.sql")
    public void should_return_user_entity_with_list_of_borrowed_book() {
        User user = CreateDataToTests.createUser();
        UserEntity savedUser = userRepository.save(user);

        Library library = CreateDataToTests.createLibrary();
        Book book1 = CreateDataToTests.createBook("isbn:1");
        Book book2 = CreateDataToTests.createBook("isbn:2");
        List<Book> books = List.of(book1, book2);
        library.setBooks(books);
        LibraryEntity savedLibrary = libraryRepository.save(library);

        Integer userId = savedUser.getId();
        Integer bookId = savedLibrary.getBooks().stream().findFirst().get().getId();

        Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
        boolean result = userJpaRepository.borrowBook(userId, bookEntity.get());

        assertThat(result).isEqualTo(true);

        UserEntity userWithBorrowedBook = userJpaRepository.findById(userId).get();

        assertThat(userWithBorrowedBook.getBorrowedBooks()).hasSize(1);

    }

    @Test
    @Sql("clean-db.sql")
    public void should_find_user_entity_and_return_user_with_borrowed_book() {
        User user = CreateDataToTests.createUser();
        UserEntity savedUser = userRepository.save(user);

        Library library = CreateDataToTests.createLibrary();
        Book book1 = CreateDataToTests.createBook("isbn:1");
        Book book2 = CreateDataToTests.createBook("isbn:2");
        List<Book> books = List.of(book1, book2);
        library.setBooks(books);
        LibraryEntity savedLibrary = libraryRepository.save(library);

        Integer userId = savedUser.getId();
        Integer bookId = savedLibrary.getBooks().stream().findFirst().get().getId();

        Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
        boolean result = userJpaRepository.borrowBook(userId, bookEntity.get());

        Optional<User> foundUser = userRepository.findById(userId);

        assertThat(foundUser).isNotEmpty();

        assertThat(foundUser.get().getBorrowedBooks()).hasSize(1);
        assertThat(foundUser.get().getBorrowedBooks().stream().findFirst().get().getBorrowingDate())
                .isEqualTo(LocalDate.now());
    }

    @Test
    @Sql("clean-db.sql")
    public void should_return_book() {
        User user = CreateDataToTests.createUser();
        UserEntity savedUser = userRepository.save(user);

        Library library = CreateDataToTests.createLibrary();
        Book book1 = CreateDataToTests.createBook("isbn:1");
        Book book2 = CreateDataToTests.createBook("isbn:2");
        List<Book> books = List.of(book1, book2);
        library.setBooks(books);
        LibraryEntity savedLibrary = libraryRepository.save(library);

        Integer userId = savedUser.getId();
        Integer bookId = savedLibrary.getBooks().stream().findFirst().get().getId();

        Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
        userJpaRepository.borrowBook(userId, bookEntity.get());


        boolean result = userRepository.returnBook(bookEntity.get().getId());
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(result).isEqualTo(true);
        assertThat(foundUser.get().getBorrowedBooks()).hasSize(0);
    }

}