package com.pl.service.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Library {

    private Integer id;
    private Integer number;
    private String address;

    List<Book> books = new ArrayList<>();

}


