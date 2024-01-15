package com.pl.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
public interface UserBookJpaRepository extends CrudRepository<UserBookEntity, Integer> {

    @Query(value = "select * from user_book ub where ub.borrowing_date=:alertDate",
            nativeQuery = true)
    List<UserBookEntity> findByAlertDate(LocalDate alertDate);
}
