package com.pl.service.service;

import com.pl.repository.LibraryRepository;
import com.pl.service.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private LibraryRepository libraryRepository;

    @InjectMocks
    private ReportService reportService;


    @Test
    public void should_return_list_of_books_with_alert_date() {
        //given
        Book book1 = new Book();
        book1.setBorrowingDate(LocalDate.of(2023, 12, 21));
        Book book2 = new Book();
        book2.setBorrowingDate(LocalDate.of(2023, 12, 21));
        Book book3 = new Book();
        book3.setBorrowingDate(LocalDate.of(2024, 01, 10));

        List<Book> books = List.of(book1, book2, book3);
        List<Book> booksWithAlertDate = List.of(book1, book2);

        LocalDate alertDate = LocalDate.of(2023, 12, 21);

        Mockito.when(libraryRepository.findBooksByAlertDate(Mockito.eq(alertDate))).thenReturn(booksWithAlertDate);

        //when
        reportService.findBookWithAlertReturnDate(5);

        //then
        Mockito.verify(libraryRepository, Mockito.atLeastOnce()).findBooksByAlertDate(Mockito.eq(alertDate));
    }


}