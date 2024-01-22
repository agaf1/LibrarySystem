package com.pl.repository;

import com.pl.service.domain.Book;
import com.pl.service.domain.Library;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LibraryRepository {
    private final LibraryJpaRepository libraryJpaRepository;
    private final LibraryMapper libraryMapper;
    private final BookRepository bookRepository;
    private final UserBookJpaRepository userBookJpaRepository;

    @Transactional
    public Library save(Library library) {
        return libraryMapper.mapFromEntity(Optional.of
                (libraryJpaRepository.save(libraryMapper.mapToEntity(library)))).get();
    }

    @Transactional(readOnly = true)
    public Optional<Library> findById(Integer libraryId) {

        Optional<LibraryEntity> libraryEntity = libraryJpaRepository.findById(libraryId);

        return libraryMapper.mapFromEntity(libraryEntity);
    }

    @Transactional(readOnly = true)
    public List<Library> findAll() {
        List<Library> libraries = new ArrayList<>();
        for (LibraryEntity libraryEntity : libraryJpaRepository.findAll()) {
            libraries.add(libraryMapper.map(libraryEntity));
        }
        return libraries;
    }

    @Transactional
    public List<Book> findAllBooks(Integer libraryId) {
        return bookRepository.findAllBooks(libraryId);
    }

    @Transactional
    public void addBook(Integer libraryId, Integer bookId) {
        Optional<LibraryEntity> libraryEntity = libraryJpaRepository.findById(libraryId);
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            libraryEntity.get().addBook(libraryMapper.map(book.get()));
            libraryJpaRepository.save(libraryEntity.get());
        }
    }

    @Transactional
    public boolean deleteBook(Integer libraryId, Integer bookId) {
        return libraryJpaRepository.deleteBook(libraryId, bookId);
    }

    @Transactional
    public List<Book> findBooksByAlertDate(LocalDate alertDate) {

        List<BookEntity> userBookEntities = userBookJpaRepository.findByAlertDate(alertDate);

        List<Book> booksWithAlertDate = new ArrayList<>();
        for (BookEntity bookEntity : userBookEntities) {
            Book book = libraryMapper.mapToBookFromEntity(bookEntity);
            book.setLibrary(libraryMapper.map(bookEntity.getLibrary()));
            booksWithAlertDate.add(book);
        }
        return booksWithAlertDate;
    }


}
