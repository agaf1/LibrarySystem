package com.pl.controller;

import com.pl.service.domain.Book;
import com.pl.service.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping(path = "/book/create")
    public Book create(@ModelAttribute BookDTO bookDTO) {
        Book bookToCreate = BookDTOMapper.INSTANCE.map(bookDTO);
        bookService.createBook(bookToCreate);
        return bookToCreate;
    }

    @GetMapping(path = "/book/getByAuthor/{author}")
    public Map<LibraryDTO, BookDTO> getBooksByAuthor(@PathVariable String author) {
        Map<LibraryDTO, BookDTO> result = new HashMap<>();
        for (Book book : bookService.findBooksByAuthor(author)) {
            result.put(LibraryDTOMapper.INSTANCE.map(book.getLibrary()), BookDTOMapper.INSTANCE.map(book));
        }
        return result;
    }

    @GetMapping(path = "/book/getByTitle/{title}")
    public Map<LibraryDTO, BookDTO> getBooksByTitle(@PathVariable String title) {
        Map<LibraryDTO, BookDTO> result = new HashMap<>();
        for (Book book : bookService.findBooksByTitle(title)) {
            result.put(LibraryDTOMapper.INSTANCE.map(book.getLibrary()), BookDTOMapper.INSTANCE.map(book));
        }
        return result;
    }


}
