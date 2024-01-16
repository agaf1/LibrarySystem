package com.pl.repository;

import com.pl.service.domain.Book;
import com.pl.service.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;
    private final BookRepository bookRepository;
    private final UserBookJpaRepository userBookJpaRepository;

    @Transactional
    public User save(User user) {
        UserEntity userEntity = userJpaRepository.save(userMapper.map(user));
        User user1 = userMapper.map(userEntity);
        return user1;
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Integer userId) {
        Optional<UserEntity> userEntity = userJpaRepository.findById(userId);
        if (userEntity.isPresent()) {
            return Optional.of(userMapper.mapFromEntity(userEntity.get()));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public boolean borrowBook(Integer userId, Integer bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            return userJpaRepository.borrowBook(userId, userMapper.map(book.get()));
        } else {
            return false;
        }
    }

    @Transactional
    public boolean returnBook(Integer bookId) {
//        Optional<Book> book = bookRepository.findById(bookId);
//        BookEntity bookEntity = null;
//        if (book.isPresent()) {
//            bookEntity = userMapper.map(book.get());
//        }
//        return userJpaRepository.returnBook(bookEntity);
        Optional<BookEntity> bookEntity = bookRepository.findEntityById(bookId);
        if (bookEntity.isPresent()) {
            return userJpaRepository.returnBook(bookEntity.get());
        }
        return false;
    }


}


