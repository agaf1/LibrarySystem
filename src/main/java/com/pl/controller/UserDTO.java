package com.pl.controller;

import com.pl.service.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@NoArgsConstructor
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;

    @Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
    public static interface UserDTOMapper {

        UserDTOMapper INSTANCE = Mappers.getMapper(UserDTOMapper.class);

        User map(UserDTO userDTO);

        UserDTO map(User user);
    }
}
