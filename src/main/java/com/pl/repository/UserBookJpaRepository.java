package com.pl.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
public interface UserBookJpaRepository extends CrudRepository<UserBookEntity, Integer> {

    @Query(
            "select b from BookEntity b join b.userBook ub where ub.borrowingDate=:alertDate  "
    )
    List<BookEntity> findByAlertDate(LocalDate alertDate);

    @Query(
            value = "select * from user_book where book_id=:bookId  ", nativeQuery = true
    )
    UserBookEntity findByBookId(Integer bookId);

}
