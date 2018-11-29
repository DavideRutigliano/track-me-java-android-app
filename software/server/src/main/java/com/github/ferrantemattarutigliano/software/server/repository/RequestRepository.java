package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.RequestEntity;
import com.github.ferrantemattarutigliano.software.server.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository<R extends RequestEntity> extends JpaRepository<R, Long> {
}
