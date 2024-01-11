package com.pl.repository;

import com.pl.service.domain.Book;
import com.pl.service.domain.Library;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LibraryRepositoryTest {
    @Autowired
    private LibraryJpaRepository libraryJpaRepository;
    @Autowired
    private BookJpaRepository bookJpaRepository;
    @Autowired
    private BookRepository bookRepository;

    private final LibraryMapper libraryMapper = LibraryMapper.INSTANCE;

    private LibraryRepository libraryRepository;

    @BeforeEach
    void setup() {
        libraryRepository = new LibraryRepository(libraryJpaRepository, libraryMapper, bookRepository);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_save_library_and_return_library_entity() {
        Library library = CreateDataToTests.createLibrary();
        Book book1 = CreateDataToTests.createBook("isbn:1");
        Book book2 = CreateDataToTests.createBook("isbn:2");
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        library.setBooks(books);

        LibraryEntity savedLibrary = libraryRepository.save(library);

        Set<BookEntity> savedBooks = savedLibrary.getBooks();
        BookEntity savedBook1 = savedBooks.stream().findFirst().get();

        assertThat(savedLibrary.getNumber()).isEqualTo(1);
        assertThat(savedLibrary.getAddress()).isEqualTo("bakerStr");
        assertThat(savedLibrary.getId()).isNotNull();
        assertThat(savedLibrary.getBooks()).hasSize(2);
        assertThat(savedBook1.getLibrary().getId()).isNotNull();
    }

    @Test
    @Sql("clean-db.sql")
    public void should_find_Library_entity_and_return_Library() {
        Library library = CreateDataToTests.createLibrary();
        Book book1 = CreateDataToTests.createBook("isbn:1");
        Book book2 = CreateDataToTests.createBook("isbn:2");
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        library.setBooks(books);

        LibraryEntity savedLibrary = libraryRepository.save(library);

        Integer libraryId = savedLibrary.getId();

        Optional<Library> foundLibrary = libraryRepository.findById(libraryId);

        assertThat(foundLibrary).isNotEmpty();
        assertThat(foundLibrary.get().getNumber()).isEqualTo(1);
        assertThat(foundLibrary.get().getAddress()).isEqualTo("bakerStr");
        assertThat(foundLibrary.get().getBooks()).hasSize(2);
        assertThat(foundLibrary.get().getBooks().stream().findFirst().get().getLibrary().getId()).isEqualTo(1);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_add_book_to_existing_Library() {
        Library library = CreateDataToTests.createLibrary();
        LibraryEntity savedLibrary = libraryRepository.save(library);

        Book book = CreateDataToTests.createBook("isbn:1");
        BookEntity savedBook = bookJpaRepository.save(libraryMapper.map(book));

        libraryRepository.addBook(savedLibrary.getId(), savedBook.getId());

        Optional<LibraryEntity> foundLibrary = libraryJpaRepository.findById(savedLibrary.getId());

        assertThat(foundLibrary.get().getBooks()).hasSize(1);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_delete_existing_book_from_Library() {
        Library library = CreateDataToTests.createLibrary();
        Book book1 = CreateDataToTests.createBook("isbn:1");
        Book book2 = CreateDataToTests.createBook("isbn:2");
        List<Book> books = List.of(book1, book2);
        library.setBooks(books);
        LibraryEntity savedLibrary = libraryRepository.save(library);

        Integer libraryId = savedLibrary.getId();
        Integer bookId = savedLibrary.getBooks().stream().findFirst().get().getId();

        boolean result = libraryRepository.deleteBook(libraryId, bookId);

        Optional<LibraryEntity> foundLibrary = libraryJpaRepository.findById(libraryId);

        assertThat(result).isEqualTo(true);
        assertThat(foundLibrary.get().getBooks()).hasSize(1);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_not_delete_book_from_Library_because_this_book_is_not_exists() {
        Library library = CreateDataToTests.createLibrary();
        Book book1 = CreateDataToTests.createBook("isbn:1");
        Book book2 = CreateDataToTests.createBook("isbn:2");
        List<Book> books = List.of(book1, book2);
        library.setBooks(books);
        LibraryEntity savedLibrary = libraryRepository.save(library);

        Integer libraryId = savedLibrary.getId();
        Integer bookId = 5;

        boolean result = libraryRepository.deleteBook(libraryId, bookId);

        Optional<LibraryEntity> foundLibrary = libraryJpaRepository.findById(libraryId);

        assertThat(result).isEqualTo(false);
        assertThat(foundLibrary.get().getBooks()).hasSize(2);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_find_all_libraries_from_DB() {
        Library library1 = CreateDataToTests.createLibrary();
        Library library2 = CreateDataToTests.createLibrary();
        Library library3 = CreateDataToTests.createLibrary();

        LibraryEntity savedLibrary1 = libraryRepository.save(library1);
        LibraryEntity savedLibrary2 = libraryRepository.save(library2);
        LibraryEntity savedLibrary3 = libraryRepository.save(library3);

        List<Library> libraries = libraryRepository.findAll();

        assertThat(libraries).hasSize(3);
    }

    @Test
    @Sql("clean-db.sql")
    public void should_find_all_books_from_existing_library() {
        Library library = CreateDataToTests.createLibrary();
        Book book1 = CreateDataToTests.createBook("isbn:1");
        Book book2 = CreateDataToTests.createBook("isbn:2");
        Book book3 = CreateDataToTests.createBook("isbn:3");
        List<Book> books = List.of(book1, book2, book3);
        library.setBooks(books);
        LibraryEntity savedLibrary = libraryRepository.save(library);

        List<Book> booksFromLibrary = libraryRepository.findAllBooks(savedLibrary.getId());

        assertThat(booksFromLibrary).hasSize(3);
    }
}