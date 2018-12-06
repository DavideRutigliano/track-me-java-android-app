package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.GroupRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long> {
    Collection<GroupRequest> findByThirdParty(Long id);
}
