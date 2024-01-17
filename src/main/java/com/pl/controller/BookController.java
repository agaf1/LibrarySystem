package com.pl.controller;

import com.pl.service.domain.Book;
import com.pl.service.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
