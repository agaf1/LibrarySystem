package com.pl.repository;

import com.pl.service.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {

    BookEntity map(Book book);

    Book map(BookEntity bookEntity);
}
