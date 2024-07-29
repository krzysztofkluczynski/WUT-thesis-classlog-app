package com.example.classlog.mapper;

import com.example.classlog.dto.SignUpDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}