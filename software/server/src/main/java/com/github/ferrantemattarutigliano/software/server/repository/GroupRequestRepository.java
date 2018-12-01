package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long> {
}