package com.pl.controller;

import com.pl.service.domain.Book;
import com.pl.service.domain.Library;
import com.pl.service.service.LibraryService;
import com.pl.service.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LibraryController {

    @Autowired
    private LibraryService libraryService;
    @Autowired
    private ReportService reportService;

    @PostMapping(path = "/library/create")
    public Library create(@ModelAttribute LibraryDTO libraryDTO) {
        Library libraryToCreate = LibraryDTOMapper.INSTANCE.map(libraryDTO);
        libraryService.createLibrary(libraryToCreate);
        return libraryToCreate;
    }

    @PostMapping(path = "/library/{idLibrary}/addBook/{idBook}")
    public void addBook(@PathVariable Integer idLibrary, @PathVariable Integer idBook) {
        libraryService.addBook(idLibrary, idBook);
    }

    @PostMapping(path = "/library/{idLibrary}/deleteBook/{idBook}")
    public void deleteBook(@PathVariable Integer idLibrary, @PathVariable Integer idBook) {
        libraryService.deleteBook(idLibrary, idBook);
    }

    @GetMapping(path = "/library/getAll")
    public List<LibraryDTO> getAllLibraries() {
        List<LibraryDTO> librariesDTO = new ArrayList<>();
        for (Library library : libraryService.getLibraries()) {
            librariesDTO.add(LibraryDTOMapper.INSTANCE.map(library));
        }
        return librariesDTO;
    }

    @GetMapping(path = "library/{libraryId}/books")
    public List<BookDTO> getBooksOfThisLibrary(@PathVariable Integer libraryId) {
        List<BookDTO> booksDTO = new ArrayList<>();
        for (Book book : libraryService.getBooksOfThisLibrary(libraryId)) {
            booksDTO.add(BookDTOMapper.INSTANCE.map(book));
        }
        return booksDTO;
    }

    @GetMapping(path = "/report/{alertDays}")
    public List<ReportDTO> getReports(@PathVariable Integer alertDays) {
        List<ReportDTO> reports = new ArrayList<>();
        for (Book book : reportService.findBookWithAlertReturnDate(alertDays)) {
            ReportDTO reportDTO = new ReportDTO();
            reportDTO.setBookDTO(BookDTOMapper.INSTANCE.map(book));
            reportDTO.setLibraryDTO(LibraryDTOMapper.INSTANCE.map(book.getLibrary()));
            reportDTO.setUserDTO(UserDTOMapper.INSTANCE.map(book.getUser()));
            reportDTO.setMessage("The book's return date expires in " + alertDays + " days.");
            reports.add(reportDTO);
        }
        return reports;
    }
}
