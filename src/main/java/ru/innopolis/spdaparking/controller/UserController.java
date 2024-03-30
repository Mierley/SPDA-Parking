package ru.innopolis.spdaparking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.innopolis.spdaparking.dto.UserDto;
import ru.innopolis.spdaparking.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class UserController {


    private final UserService service;

    
    @GetMapping("/user")
    public ResponseEntity<List<UserDto>> getAllUsernames() {
        return ResponseEntity.ok(service.getUsernames());
    }
    
    @PostMapping("/user")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        
        return ResponseEntity.ok(service.createUser(user));
    }
}
