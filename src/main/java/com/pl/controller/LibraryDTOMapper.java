package com.pl.controller;

import com.pl.service.domain.Library;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LibraryDTOMapper {

    LibraryDTOMapper INSTANCE = Mappers.getMapper(LibraryDTOMapper.class);

    Library map(LibraryDTO libraryDTO);

    LibraryDTO map(Library library);
}
