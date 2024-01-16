package com.pl.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserBookRepository {

    private final UserBookJpaRepository userBookJpaRepository;

    @Transactional
    List<UserBookEntity> findByAlertDate(LocalDate alertDate) {
        return userBookJpaRepository.findByAlertDate(alertDate);
    }

    @Transactional
    Optional<UserBookEntity> findById(Integer userBookId) {
        return userBookJpaRepository.findById(userBookId);
    }

    @Transactional
    UserBookEntity save(UserBookEntity userBookEntity) {
        return userBookJpaRepository.save(userBookEntity);
    }
}
