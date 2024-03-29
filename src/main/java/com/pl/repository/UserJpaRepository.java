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
//            save(userToSave);
            return true;
        }
        return false;
    }

    @Transactional
    default boolean returnBook(BookEntity bookEntity) {
        if (bookEntity != null) {
            var userBook = bookEntity.getUserBook();
            if (userBook != null) {
                userBook.getUser().returnBook(userBook.getBook());
                return true;
            }
        }
        return false;
    }

}
