package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@Repository
@NoRepositoryBean
public interface UserRepository<U extends User> extends JpaRepository<U, Long> {
    U findByEmail(String email);

    @Query("select u from #{#entityName} where u.username = ?1")
    boolean findUsername(String username);
}
