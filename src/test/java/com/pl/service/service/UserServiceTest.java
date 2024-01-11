package com.pl.service.service;

import com.pl.repository.UserRepository;
import com.pl.service.domain.Book;
import com.pl.service.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void should_create_new_user() {
        User user = createUser();

        userService.create(user);

        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(user);
    }

    @Test
    public void should_borrow_book() {
        Integer userId = 1;
        Integer bookId = 1;
        Integer maxBooks = 5;
        User user = createUser();
        user.setId(userId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.borrowBook(userId, bookId)).thenReturn(true);

        userService.borrowBook(userId, bookId, maxBooks);

        Mockito.verify(userRepository, Mockito.atLeastOnce()).borrowBook(userId, bookId);
    }

    @Test
    public void should_not_borrow_book_because_book_is_not_exist() {
        Integer userId = 1;
        Integer bookId = 1;
        Integer maxBooks = 5;
        User user = createUser();
        user.setId(userId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.borrowBook(userId, bookId)).thenReturn(false);

        assertThatThrownBy(() -> userService.borrowBook(userId, bookId, maxBooks))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book with id " + bookId + " not exists");
    }

    @Test
    public void should_not_borrow_book_because_user_is_not_exist() {
        Integer userId = 1;
        Integer bookId = 1;
        Integer maxBooks = 5;
        User user = createUser();
        user.setId(userId);
        List<Book> books = List.of(createBook("1"), createBook("2")
                , createBook("3"), createBook("4"), createBook("5"));
        user.setBorrowedBooks(books);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.borrowBook(userId, bookId, maxBooks))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class)
                .hasMessage("You have already borrowed the maximum number of books");
    }

    @Test
    public void should_not_borrow_book_because_user_borrowed_already_max_books() {
        Integer userId = 1;
        Integer bookId = 1;
        Integer maxBooks = 5;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.borrowBook(userId, bookId, maxBooks))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User with id " + userId + " not exists");
    }

    @Test
    public void should_return_book() {
        Integer bookId = 1;

        userService.returnBook(bookId);

        Mockito.verify(userRepository, Mockito.atLeastOnce()).returnBook(bookId);
    }

    @Test
    public void should_extend_returning_date_of_borrowing_book() {
        Integer userId = 1;
        Integer bookId = 1;
        User user = createUser();
        user.setId(1);
        Book book1 = createBook("1");
        book1.setId(1);
        book1.setBorrowingDate(LocalDate.of(2023, 12, 12));
        Book book2 = createBook("2");
        book2.setId(2);
        book2.setBorrowingDate(LocalDate.of(2023, 10, 10));
        List<Book> books = List.of(book1, book2);
        user.setBorrowedBooks(books);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        LocalDate result = userService.extendTimeOfBorrowBook(userId, bookId);

        Mockito.verify(userRepository, Mockito.atLeastOnce()).save(user);
        assertThat(result).isEqualTo(LocalDate.of(2023, 12, 12).plusMonths(2));
    }

    private static User createUser() {
        User user = new User();
        user.setFirstName("Aga");
        user.setLastName("Fifi");
        user.setEmail("aga@aga.pl");
        return user;
    }

    private static Book createBook(String isbn) {
        Book book = new Book();
        book.setAuthor("author");
        book.setTitle("title");
        book.setIsbn(isbn);
        return book;
    }
}