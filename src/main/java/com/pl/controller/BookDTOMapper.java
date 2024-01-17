package com.pl.controller;

import com.pl.service.domain.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookDTOMapper {

    BookDTOMapper INSTANCE = Mappers.getMapper(BookDTOMapper.class);

    Book map(BookDTO bookDTO);

    BookDTO map(Book book);
}
