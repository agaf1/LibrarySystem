package com.pl.service.service;

import com.pl.repository.LibraryRepository;
import com.pl.service.domain.Book;
import com.pl.service.domain.Library;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {


    @Mock
    private LibraryRepository libraryRepository;

    @InjectMocks
    private LibraryService libraryService;


    @Test
    public void should_create_new_library() {
        Library library = createLibrary();

        libraryService.createLibrary(library);

        Mockito.verify(libraryRepository, Mockito.atLeastOnce()).save(Mockito.argThat(l -> {
            assertThat(l).isEqualTo(library);
            return true;
        }));
    }

    @Test
    public void should_add_book_to_existing_library() {
        Integer libraryId = 1;
        Integer bookId = 1;
        Library library = createLibrary();
        library.setId(libraryId);
        Book book = createBook("isbn:2");
        book.setId(bookId);

        Mockito.when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(library));

        libraryService.addBook(libraryId, bookId);

        Mockito.verify(libraryRepository, Mockito.atLeastOnce()).addBook(libraryId, bookId);
    }

    @Test
    public void should_not_add_book_when_library_is_not_existing() {
        Integer libraryId = 1;
        Integer bookId = 1;
        Book book = createBook("isbn:1");
        book.setId(bookId);

        Mockito.when(libraryRepository.findById(libraryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> libraryService.addBook(libraryId, bookId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_delete_book_from_library() {
        Integer libraryId = 1;
        Integer bookId = 1;

        Mockito.when(libraryRepository.deleteBook(libraryId, bookId)).thenReturn(true);

        libraryService.deleteBook(libraryId, bookId);

        assertThatCode(() -> libraryService.deleteBook(libraryId, bookId)).doesNotThrowAnyException();
    }

    @Test
    public void should_not_delete_book_when_this_book_is_not_exists_in_this_library() {
        Integer libraryId = 1;
        Integer bookId = 5;

        Mockito.when(libraryRepository.deleteBook(libraryId, bookId)).thenReturn(false);

        assertThatThrownBy(() -> libraryService.deleteBook(libraryId, bookId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Library with id 1 not exists or Book with id 5 not exists");
    }


    private static Library createLibrary() {
        Library library = new Library();
        library.setNumber(1);
        library.setAddress("BakerStr");
        return library;
    }

    private static Book createBook(String isbn) {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setAuthor("Steve");
        book.setTitle("Good dog");
        return book;
    }
}