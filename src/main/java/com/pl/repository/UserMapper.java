package com.pl.repository;

import com.pl.service.domain.Book;
import com.pl.service.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    default User mapFromEntity(UserEntity userEntity) {
        User user = map(userEntity);
        user.setBorrowedBooks(setBorrowedBooksToList(userEntity.getBorrowedBooks()));
        return user;
    }

    //    @Mapping(target = "borrowedBooks", ignore = true)
    UserEntity map(User user);

    @Mapping(target = "borrowedBooks", ignore = true)
    User map(UserEntity userEntity);

    //    @Mapping(target = "library", ignore = true)
    BookEntity map(Book book);

    @Mapping(target = "library", ignore = true)
    Book map(BookEntity bookEntity);

    private static List<Book> setBorrowedBooksToList(Set<UserBookEntity> borrowedBooksEntity) {
        List<Book> borrowedBooks = new ArrayList<>();
        for (UserBookEntity userBookEntity : borrowedBooksEntity) {
            Book book = UserMapper.INSTANCE.map(userBookEntity.getBook());
            book.setUser(UserMapper.INSTANCE.map(userBookEntity.getUser()));
            book.setBorrowingDate(userBookEntity.getBorrowingDate());
            book.setLibrary(LibraryMapper.INSTANCE.map(userBookEntity.getBook().getLibrary()));
            borrowedBooks.add(book);
        }
        return borrowedBooks;
    }

}
