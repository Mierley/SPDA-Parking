package ru.innopolis.spdaparking.mapper;

import org.springframework.stereotype.Component;
import ru.innopolis.spdaparking.dto.UserDto;
import ru.innopolis.spdaparking.entity.User;

@Component
public class UserMapper {
    
    public User mapToEntity(UserDto dto) {
        return User.builder()
                .name(dto.name())
                .password(dto.password())
                .build();
    }

    public UserDto mapToDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .password(user.getPassword())
                .lessons(user.getLessonNames())
                .build();
    }
    
}
