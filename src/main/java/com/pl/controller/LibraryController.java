package com.pl.controller;

import com.pl.service.domain.Library;
import com.pl.service.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryController {

    @Autowired
    private LibraryService libraryService;

    @PostMapping(path = "/library/create")
    public Library create(@ModelAttribute LibraryDTO libraryDTO) {
        Library libraryToCreate = LibraryDTOMapper.INSTANCE.map(libraryDTO);
        libraryService.createLibrary(libraryToCreate);
        return libraryToCreate;
    }

    @PostMapping(path = "/library/{libraryId}/addBook/{bookId}")
    public void addBook(@PathVariable Integer libraryId, @PathVariable Integer bookId) {
        libraryService.addBook(libraryId, bookId);
    }
}
