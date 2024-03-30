package ru.innopolis.spdaparking.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UserDto(Long id, String name, String password, List<String> lessons) {
}
