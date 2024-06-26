package ru.innopolis.spdaparking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.innopolis.spdaparking.entity.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    
}
