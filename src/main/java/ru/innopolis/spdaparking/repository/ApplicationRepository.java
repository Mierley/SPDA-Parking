package ru.innopolis.spdaparking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.innopolis.spdaparking.entity.Application;
public interface ApplicationRepository extends JpaRepository<Application, Long> {};
