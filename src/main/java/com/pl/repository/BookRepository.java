package com.pl.repository;

import com.pl.service.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    @Autowired
    private BookJpaRepository bookJpaRepository;
    @Autowired
    private LibraryMapper libraryMapper;

    @Transactional(readOnly = true)
    public Optional<BookEntity> findById(Integer bookId) {
        return bookJpaRepository.findById(bookId);
    }

    @Transactional
    public List<BookEntity> findAllBooks(Integer libraryId) {
        return bookJpaRepository.findAllBook(libraryId);
    }

    @Transactional
    public List<Book> findBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        for (BookEntity bookEntity : bookJpaRepository.findBooksByAuthor(author)) {
            Book book = libraryMapper.map(bookEntity);
            book.setLibrary(libraryMapper.map(bookEntity.getLibrary()));
            books.add(book);
        }
        return books;
    }

    @Transactional
    public List<Book> findBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        for (BookEntity bookEntity : bookJpaRepository.findBooksByAuthor(title)) {
            Book book = libraryMapper.map(bookEntity);
            book.setLibrary(libraryMapper.map(bookEntity.getLibrary()));
            books.add(book);
        }
        return books;
    }

}
