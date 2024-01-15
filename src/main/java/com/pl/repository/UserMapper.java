package com.pl.repository;

import com.pl.service.domain.Book;
import com.pl.service.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity map(User user);

    User map(UserEntity userEntity);

    BookEntity map(Book book);

    Book map(BookEntity bookEntity);

}
