package com.pl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserBookJpaRepository extends CrudRepository<UserBookEntity, Integer> {
}
