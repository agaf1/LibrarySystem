package com.pl.service.service;

import com.pl.repository.BookRepository;
import com.pl.service.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public void createBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public List<Book> findBooksByAuthor(String author) {
        return bookRepository.findBooksByAuthor(author);
    }

    @Transactional
    public List<Book> findBooksByTitle(String title) {
        return bookRepository.findBooksByAuthor(title);
    }
}
