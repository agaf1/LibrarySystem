package com.pl.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {

    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Transactional(readOnly = true)
    public Optional<BookEntity> findById(Integer bookId) {
        return bookJpaRepository.findById(bookId);
    }

    @Transactional
    public List<BookEntity> findAllBooks(Integer libraryId) {
        return bookJpaRepository.findAllBook(libraryId);
    }

}
