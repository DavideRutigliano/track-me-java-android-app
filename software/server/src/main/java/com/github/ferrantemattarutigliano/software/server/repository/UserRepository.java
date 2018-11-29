package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository<U extends UserEntity> extends JpaRepository<U, Long> {
    U findByEmail(String email);
}
