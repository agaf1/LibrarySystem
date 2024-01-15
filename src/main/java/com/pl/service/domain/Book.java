package com.pl.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class Book {

    private Integer id;

    private String isbn;
    private String author;
    
    private String title;

    private Library library;
    private User user;
    private LocalDate borrowingDate;

}
