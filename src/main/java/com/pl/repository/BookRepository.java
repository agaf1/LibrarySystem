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
    @Autowired
    private BookMapper bookMapper;

    @Transactional
    public Book save(Book book) {
        return libraryMapper.mapToBookFromEntity(bookJpaRepository.save(libraryMapper.map(book)));
    }

    @Transactional(readOnly = true)
    public Optional<Book> findById(Integer bookId) {
        Optional<BookEntity> bookEntityOptional = bookJpaRepository.findById(bookId);
        return bookEntityOptional.isPresent()
                ? Optional.of(libraryMapper.mapToBookFromEntity(bookEntityOptional.get()))
                : Optional.empty();

    }

    @Transactional(readOnly = true)
    public Optional<BookEntity> findEntityById(Integer bookId) {
        return bookJpaRepository.findById(bookId);
    }

    @Transactional
    public List<Book> findAllBooks(Integer libraryId) {
        List<Book> books = new ArrayList<>();
        for (BookEntity bookEntity : bookJpaRepository.findAllBook(libraryId)) {
            Book book = libraryMapper.mapToBookFromEntity(bookEntity);
            book.setLibrary(libraryMapper.map(bookEntity.getLibrary()));
            books.add(book);
        }
        return books;

    }

    @Transactional
    public List<Book> findBooksByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        for (BookEntity bookEntity : bookJpaRepository.findBooksByAuthor(author)) {
            Book book = libraryMapper.mapToBookFromEntity(bookEntity);
            book.setLibrary(libraryMapper.map(bookEntity.getLibrary()));
            books.add(book);
        }
        return books;
    }

    @Transactional
    public List<Book> findBooksByTitle(String title) {
        List<Book> books = new ArrayList<>();
        for (BookEntity bookEntity : bookJpaRepository.findBooksByAuthor(title)) {
            Book book = libraryMapper.mapToBookFromEntity(bookEntity);
            book.setLibrary(libraryMapper.map(bookEntity.getLibrary()));
            books.add(book);
        }
        return books;
    }

}
