package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UserRepository<U extends User> extends JpaRepository<U, Long> {
    U findByEmail(String email);
    U findByUsername(String username);
    Boolean existsByUsername(String username);
}
