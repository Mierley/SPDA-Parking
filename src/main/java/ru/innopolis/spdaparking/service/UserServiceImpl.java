package ru.innopolis.spdaparking.service;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.innopolis.spdaparking.dto.UserDto;
import ru.innopolis.spdaparking.mapper.UserMapper;
import ru.innopolis.spdaparking.repository.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final MeterRegistry registry;

    @Override
    @Timed(value = "get_users_duration", percentiles = {0.5, 0.9}, description = "some custom metrics", histogram = true)
    public List<UserDto> getUsernames() {
        registry.counter("get_users_counter").increment();
        return repository.findAll().stream()
                .map(userMapper::mapToDto)
                .toList();
    }

    @Override
    @Timed(value = "create_users_duration", percentiles = {0.5, 0.9}, description = "some custom metrics", histogram = true)
    public UserDto createUser(UserDto dto) {
        var u = userMapper.mapToEntity(dto);
        
        return userMapper.mapToDto(repository.save(u));
    }
}
