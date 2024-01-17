package com.pl.controller;

import com.pl.service.domain.User;
import com.pl.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/user/create")
    public User create(@ModelAttribute UserDTO userDTO) {
        User userToCreate = UserDTO.UserDTOMapper.INSTANCE.map(userDTO);
        userService.create(userToCreate);
        return userToCreate;
    }
}
