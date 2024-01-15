package com.pl.repository;

import com.pl.service.domain.Book;
import com.pl.service.domain.Library;
import com.pl.service.domain.User;

public class CreateDataToTests {

    static BookEntity createBookEntity(String isbn) {
        BookEntity book = new BookEntity();
        book.setIsbn(isbn);
        book.setAuthor("Steve Mann");
        book.setTitle("Good dog");
        return book;
    }

    static Book createBook(String isbn) {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setAuthor("Steve Mann");
        book.setTitle("Good dog");
        return book;
    }

    static LibraryEntity createLibraryEntity() {
        LibraryEntity library1 = new LibraryEntity();
        library1.setNumber(1);
        library1.setAddress("bakerStr");
        return library1;
    }

    static Library createLibrary() {
        Library library1 = new Library();
        library1.setNumber(1);
        library1.setAddress("bakerStr");
        return library1;
    }

    static Library createLibraryWithBooks(String... isbn) {
        Library library = createLibrary();
        for (String bookIsbn : isbn) {
            Book book = createBook(bookIsbn);
            book.setLibrary(library);
            library.getBooks().add(book);
        }
        return library;
    }

    static User createUser() {
        User user = new User();
        user.setFirstName("Ala");
        user.setLastName("Alutka");
        user.setEmail("ala@alutka.com");
        return user;
    }
}
