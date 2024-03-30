package ru.innopolis.spdaparking.service;

import ru.innopolis.spdaparking.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsernames();
    
    UserDto createUser(UserDto dto);
}
