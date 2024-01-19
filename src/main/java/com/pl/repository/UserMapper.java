package com.pl.repository;

import com.pl.service.domain.Book;
import com.pl.service.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);
    LibraryMapper LIBRARY_MAPPER = LibraryMapper.LIBRARY_MAPPER;

    default User mapFromEntity(UserEntity userEntity) {
        User user = map(userEntity);
        for (UserBookEntity userBookEntity : userEntity.getBorrowedBooks()) {
            if (userBookEntity.getBook() != null) {
                Book book = LIBRARY_MAPPER.mapToBookFromEntity(userBookEntity.getBook());
                book.setLibrary((LIBRARY_MAPPER.mapFromEntity(Optional.of(userBookEntity.getBook().getLibrary()))).get());
                user.getBorrowedBooks().add(book);
            }
        }

        return user;
    }

    @Mapping(target = "borrowedBooks", ignore = true)
    User map(UserEntity userEntity);

    UserEntity map(User user);

    BookEntity map(Book book);

    @Mapping(target = "library", ignore = true)
    Book map(BookEntity bookEntity);

}
