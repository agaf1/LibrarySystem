package com.pl.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "user_book")
@Getter
@Setter
public class UserBookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "book_id")
    private BookEntity book;

    @Column(name = "borrowing_date")
    private LocalDate borrowingDate;

    UserBookEntity(){}

}
