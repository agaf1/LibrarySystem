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
class LibraryRepositoryTest {
    @Autowired
    private LibraryJpaRepository libraryJpaRepository;
    @Autowired
    private BookJpaRepository bookJpaRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserBookRepository userBookRepository;
    @Autowired
    private UserRepository userRepository;

    private final LibraryMapper libraryMapper = LibraryMapper.INSTANCE;

    private LibraryRepository libraryRepository;

    @BeforeEach
    void setup() {
        libraryRepository = new LibraryRepository(libraryJpaRepository, libraryMapper, bookRepository, userBookRepository);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_save_library() {
        //given
        Library library = CreateDataToTests.createLibraryWithBooks("isbn:1", "isbn:2");

        //when
        Library savedLibrary = libraryRepository.save(library);

        //then
        List<Book> savedBooks = savedLibrary.getBooks();
        Book savedBook1 = savedBooks.stream().findFirst().get();

        assertThat(savedLibrary.getNumber()).isEqualTo(1);
        assertThat(savedLibrary.getAddress()).isEqualTo("bakerStr");
        assertThat(savedLibrary.getId()).isNotNull();
        assertThat(savedLibrary.getBooks()).hasSize(2);
        assertThat(savedBook1.getLibrary().getId()).isNotNull();
    }

    @Test
    @Sql("clean-db.sql")
    public void should_find_Library() {
        //given
        Library library = CreateDataToTests.createLibraryWithBooks("isbn:1", "isbn:2");
        Library savedLibrary = libraryRepository.save(library);
        Integer libraryId = savedLibrary.getId();

        //when
        Optional<Library> foundLibrary = libraryRepository.findById(libraryId);

        //then
        assertThat(foundLibrary).isNotEmpty();
        assertThat(foundLibrary.get().getNumber()).isEqualTo(1);
        assertThat(foundLibrary.get().getAddress()).isEqualTo("bakerStr");
        assertThat(foundLibrary.get().getBooks()).hasSize(2);
        assertThat(foundLibrary.get().getBooks().stream().findFirst().get().getLibrary().getId()).isEqualTo(1);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_add_book_to_existing_Library() {
        //given
        Library library = CreateDataToTests.createLibrary();
        Library savedLibrary = libraryRepository.save(library);

        Book book = CreateDataToTests.createBook("isbn:1");
        BookEntity savedBook = bookJpaRepository.save(libraryMapper.map(book));

        //when
        libraryRepository.addBook(savedLibrary.getId(), savedBook.getId());

        //then
        Optional<Library> foundLibrary = libraryRepository.findById(savedLibrary.getId());
        assertThat(foundLibrary.get().getBooks()).hasSize(1);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_delete_existing_book_from_Library() {
        //given
        Library library = CreateDataToTests.createLibraryWithBooks("isbn:1", "isbn:2");
        Library savedLibrary = libraryRepository.save(library);

        Integer libraryId = savedLibrary.getId();
        Integer bookId = savedLibrary.getBooks().stream().findFirst().get().getId();

        //when
        boolean result = libraryRepository.deleteBook(libraryId, bookId);

        //then
        Optional<Library> foundLibrary = libraryRepository.findById(libraryId);

        assertThat(result).isEqualTo(true);
        assertThat(foundLibrary.get().getBooks()).hasSize(1);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_not_delete_book_from_Library_because_this_book_is_not_exists() {
        //given
        Library library = CreateDataToTests.createLibraryWithBooks("isbn:1", "isbn:2");
        Library savedLibrary = libraryRepository.save(library);

        Integer libraryId = savedLibrary.getId();
        Integer bookId = 5;

        //when
        boolean result = libraryRepository.deleteBook(libraryId, bookId);

        //then
        Optional<LibraryEntity> foundLibrary = libraryJpaRepository.findById(libraryId);

        assertThat(result).isEqualTo(false);
        assertThat(foundLibrary.get().getBooks()).hasSize(2);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_find_all_libraries_from_DB() {
        //given
        Library library1 = CreateDataToTests.createLibrary();
        Library library2 = CreateDataToTests.createLibrary();
        Library library3 = CreateDataToTests.createLibrary();

        Library savedLibrary1 = libraryRepository.save(library1);
        Library savedLibrary2 = libraryRepository.save(library2);
        Library savedLibrary3 = libraryRepository.save(library3);

        //when
        List<Library> libraries = libraryRepository.findAll();

        //then
        assertThat(libraries).hasSize(3);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_find_all_books_from_existing_library() {
        //given
        Library library = CreateDataToTests.createLibraryWithBooks("isbn:1", "isbn:2", "isbn:3");
        Library savedLibrary = libraryRepository.save(library);

        //when
        List<Book> booksFromLibrary = libraryRepository.findAllBooks(savedLibrary.getId());

        //then
        assertThat(booksFromLibrary).hasSize(3);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_find_list_of_books_with_alert_return_date() {
        //given
        Library library = CreateDataToTests.createLibraryWithBooks("isbn:1", "isbn:2", "isbn:3");
        Library savedLibrary = libraryRepository.save(library);

        User user1 = CreateDataToTests.createUser();
        User user2 = CreateDataToTests.createUser();
        User savedUser1 = userRepository.save(user1);
        User savedUser2 = userRepository.save(user2);

        List<Book> booksInLibrary = savedLibrary.getBooks();
        Integer book1Id = booksInLibrary.get(0).getId();
        Integer book2Id = booksInLibrary.get(1).getId();
        Integer book3Id = booksInLibrary.get(2).getId();

        userRepository.borrowBook(savedUser1.getId(), book1Id);
        userRepository.borrowBook(savedUser1.getId(), book2Id);
        userRepository.borrowBook(savedUser2.getId(), book3Id);

        Book book2 = bookRepository.findById(book2Id).get();
        book2.setBorrowingDate(LocalDate.now().minusDays(10));
        bookRepository.save(book2);

        //when
        List<Book> bookWithAlertDay = libraryRepository.findBooksByAlertDate(LocalDate.now());

        //then
        assertThat(bookWithAlertDay).hasSize(2);
    }
}