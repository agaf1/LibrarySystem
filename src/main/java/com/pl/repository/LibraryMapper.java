package com.pl.repository;

import com.pl.service.domain.Book;
import com.pl.service.domain.Library;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.Optional;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface LibraryMapper {

    LibraryMapper INSTANCE = Mappers.getMapper(LibraryMapper.class);

    default LibraryEntity mapToEntity(Library library) {
        LibraryEntity libraryEntity = map(library);
        libraryEntity.getBooks().forEach(book -> addLibraryToBook(book, libraryEntity));
        return libraryEntity;
    }

    default Optional<Library> mapFromEntity(Optional<LibraryEntity> libraryEntity) {
        if (libraryEntity.isEmpty()) {
            return Optional.empty();
        }
        Library library = map(libraryEntity.get());
        library.getBooks().forEach(book -> setLibraryToBook(book, library));
        return Optional.of(library);
    }


    LibraryEntity map(Library library);

    Library map(LibraryEntity library);

    @Mapping(target = "library", ignore = true)
    BookEntity map(Book book);

    @Mapping(target = "library", ignore = true)
    Book map(BookEntity bookEntity);

    private static void addLibraryToBook(BookEntity book, LibraryEntity library) {
        book.setLibrary(library);
    }

    private static void setLibraryToBook(Book book, Library library) {
        book.setLibrary(library);
    }
}
