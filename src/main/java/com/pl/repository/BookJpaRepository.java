package com.pl.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
interface BookJpaRepository extends CrudRepository<BookEntity, Integer> {

    @Query(value = "SELECT * FROM  books b WHERE  b.library_id=:libraryId"
            , nativeQuery = true)
    List<BookEntity> findAllBook(Integer libraryId);

    @Query(value = "SELECT * FROM  books b WHERE  b.author=:author"
            , nativeQuery = true)
    List<BookEntity> findBooksByAuthor(String author);

    @Query(value = "SELECT * FROM  books b WHERE  b.title=:title"
            , nativeQuery = true)
    List<BookEntity> findBooksByTitle(String title);

}
