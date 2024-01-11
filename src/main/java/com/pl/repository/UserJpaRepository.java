package com.pl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
interface UserJpaRepository extends CrudRepository<UserEntity, Integer> {


    @Transactional
    default boolean borrowBook(Integer userId, BookEntity bookEntity) {
        Optional<UserEntity> userEntity = findById(userId);
        UserEntity userToSave = null;
        if (userEntity.isPresent()) {
            userToSave = userEntity.get();
            userToSave.borrowBook(bookEntity);
            save(userToSave);
            return true;
        }
        return false;
    }

    @Transactional
    default UserBookEntity returnBook(BookEntity bookEntity) {
        UserEntity user = bookEntity.getUserBook().getUser();
        if (user != null) {
            return user.returnBook(bookEntity);
        }
        return null;
    }

}
