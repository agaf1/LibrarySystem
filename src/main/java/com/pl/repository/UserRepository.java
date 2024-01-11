package com.pl.repository;

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
    public UserEntity save(User user) {
        return userJpaRepository.save(userMapper.map(user));
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Integer userId) {
        Optional<UserEntity> userEntity = userJpaRepository.findById(userId);
        if (userEntity.isPresent()) {
            return Optional.of(userMapper.map(userEntity.get()));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public boolean borrowBook(Integer userId, Integer bookId) {
        Optional<BookEntity> bookEntity = bookRepository.findById(bookId);
        if (bookEntity.isPresent()) {
            return userJpaRepository.borrowBook(userId, bookEntity.get());
        } else {
            return false;
        }
    }

    @Transactional
    public boolean returnBook(Integer bookId) {
        Optional<BookEntity> bookToReturn = bookRepository.findById(bookId);
        if (bookToReturn.isPresent()) {
            UserBookEntity userBookEntity = userJpaRepository.returnBook(bookToReturn.get());
            userBookJpaRepository.delete(userBookEntity);
            return true;
        }
        return false;
    }


}


