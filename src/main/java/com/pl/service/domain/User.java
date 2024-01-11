package com.pl.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;

    List<Book> borrowedBooks = new ArrayList<>();

}
