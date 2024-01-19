package com.pl.controller;

import com.pl.service.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDTOMapper {

    UserDTOMapper INSTANCE = Mappers.getMapper(UserDTOMapper.class);

    User map(UserDTO userDTO);

    UserDTO map(User user);
}

