package com.pl.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@NoArgsConstructor
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;

}
