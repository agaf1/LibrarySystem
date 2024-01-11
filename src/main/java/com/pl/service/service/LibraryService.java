package com.pl.service.service;

import com.pl.repository.LibraryRepository;
import com.pl.service.domain.Book;
import com.pl.service.domain.Library;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;


    public void createLibrary(Library newlibrary) {
        libraryRepository.save(newlibrary);
    }

    @Transactional
    public void addBook(Integer libraryId, Integer bookId) {
        Optional<Library> actualLibrary = libraryRepository.findById(libraryId);
        if (actualLibrary.isPresent()) {
            libraryRepository.addBook(libraryId, bookId);
        } else {
            throw new IllegalArgumentException("Library with id " + libraryId + " not exists");
        }
    }

    @Transactional
    public void deleteBook(Integer libraryId, Integer bookId) {
        boolean result = libraryRepository.deleteBook(libraryId, bookId);
        if (result == false) {
            throw new IllegalArgumentException("Library with id " + libraryId + " not exists or " +
                    "Book with id " + bookId + " not exists");
        }
    }

    @Transactional
    public List<Library> getLibraries() {
        return libraryRepository.findAll();
    }

    @Transactional
    public List<Book> getBooksOfThisLibrary(Integer libraryId) {
        return libraryRepository.findAllBooks(libraryId);
    }


}
