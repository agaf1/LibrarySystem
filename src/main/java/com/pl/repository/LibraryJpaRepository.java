package com.pl.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@Transactional
interface LibraryJpaRepository extends CrudRepository<LibraryEntity, Integer> {

    default boolean deleteBook(Integer libraryId, Integer bookId) {
        Optional<LibraryEntity> actualLibrary = findById(libraryId);
        boolean result = false;
        if (actualLibrary.isPresent()) {
            LibraryEntity libraryToSave = actualLibrary.get();
            Optional<BookEntity> bookEntity = libraryToSave.getBooks()
                    .stream()
                    .filter(b -> b.getId().equals(bookId))
                    .findAny();
            if (bookEntity.isPresent()) {
                BookEntity bookToDelete = bookEntity.get();
                libraryToSave.deleteBook(bookToDelete);
                result = true;
            }
        }
        return result;
    }
}
