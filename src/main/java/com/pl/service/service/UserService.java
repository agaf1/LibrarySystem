package com.pl.service.service;

import com.pl.repository.UserRepository;
import com.pl.service.domain.Book;
import com.pl.service.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void create(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void borrowBook(Integer userId, Integer bookId, Integer maxBooks) {
        Optional<User> actualUser = userRepository.findById(userId);
        if (actualUser.isPresent()) {
            Integer actualBorrowBooks = actualUser.get().getBorrowedBooks().size();
            if (actualBorrowBooks < maxBooks) {
                boolean result = userRepository.borrowBook(userId, bookId);
                if (result == false) {
                    throw new IllegalArgumentException("Book with id " + bookId + " not exists");
                }
            } else {
                throw new ArrayIndexOutOfBoundsException("You have already borrowed the maximum number of books");
            }
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not exists");
        }
    }


    @Transactional
    public boolean returnBook(Integer bookId) {
        return userRepository.returnBook(bookId);
    }

    @Transactional
    public LocalDate extendTimeOfBorrowBook(Integer userId, Integer bookId) {
        LocalDate newDateOfReturnOfThisBook = LocalDate.now();

        Optional<User> actualUser = userRepository.findById(userId);

        if (actualUser.isPresent()) {
            Optional<Book> actualBook = actualUser.get().getBorrowedBooks()
                    .stream()
                    .filter(b -> b.getId().equals(bookId))
                    .findAny();
            if (actualBook.isPresent()) {
                newDateOfReturnOfThisBook = userRepository.extendTimeOfBorrowBook(bookId);
            } else {
                throw new IllegalArgumentException(
                        "Book with id " + bookId + " not exists or is borrowed by someone else");
            }
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not exists");
        }
        return newDateOfReturnOfThisBook;
    }
}


