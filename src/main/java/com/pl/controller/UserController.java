package com.pl.controller;

import com.pl.service.domain.User;
import com.pl.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/user/create")
    public User create(@ModelAttribute UserDTO userDTO) {
        User userToCreate = UserDTOMapper.INSTANCE.map(userDTO);
        userService.create(userToCreate);
        return userToCreate;
    }

    @PostMapping(path = "/user/{userId}/borrowBook/{bookId}")
    public void borrowBook(@PathVariable Integer userId, @PathVariable Integer bookId) {
        userService.borrowBook(userId, bookId, 5);
    }

    @PostMapping(path = "/user/{userId}/returnBook/{bookId}")
    public void returnBook(@PathVariable Integer bookId) {
        userService.returnBook(bookId);
    }

    @PostMapping(path = "/user/{userId}/extendBook/{bookId}")
    public LocalDate extendTimeOfBorrowBook(@PathVariable Integer userId, @PathVariable Integer bookId) {
        return userService.extendTimeOfBorrowBook(userId, bookId);
    }
}
