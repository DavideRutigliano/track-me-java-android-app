package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource
@NoRepositoryBean
public interface UserRepository<U extends UserEntity> extends JpaRepository<U, Long> {
    U findByUsername(String username);

    @Query("select u from #{#entityName} where u.username = ?1")
    boolean findUsername(String username);
}
