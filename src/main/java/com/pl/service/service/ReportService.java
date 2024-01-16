package com.pl.service.service;

import com.pl.repository.LibraryRepository;
import com.pl.service.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service

public class ReportService {

    @Autowired
    private LibraryRepository libraryRepository;

    @Transactional
    public List<Book> findBookWithAlertReturnDate(Integer alertDay) {

        LocalDate alertDate = LocalDate.now().minusMonths(1).plusDays(alertDay);

        List<Book> booksWithAlertDate = libraryRepository.findBooksByAlertDate(alertDate);

        return booksWithAlertDate;
    }

}
