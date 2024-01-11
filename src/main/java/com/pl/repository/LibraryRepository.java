package com.pl.repository;

import com.pl.service.domain.Book;
import com.pl.service.domain.Library;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LibraryRepository {
    private final LibraryJpaRepository libraryJpaRepository;
    private final LibraryMapper libraryMapper;
    private final BookRepository bookRepository;


    @Transactional
    public LibraryEntity save(Library library) {
        return libraryJpaRepository.save(libraryMapper.mapToEntity(library));
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
        List<Book> books = new ArrayList<>();
        List<BookEntity> foundBooks = bookRepository.findAllBooks(libraryId);
        for (BookEntity book : foundBooks) {
            books.add(libraryMapper.map(book));
        }
        return books;
    }

    @Transactional
    public void addBook(Integer libraryId, Integer bookId) {
        Optional<LibraryEntity> libraryEntity = libraryJpaRepository.findById(libraryId);
        Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
        if (bookEntity.isPresent()) {
            libraryEntity.get().addBook(bookEntity.get());
            libraryJpaRepository.save(libraryEntity.get());
        }
    }

    @Transactional
    public boolean deleteBook(Integer libraryId, Integer bookId) {
        return libraryJpaRepository.deleteBook(libraryId, bookId);
    }


}
