package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface IndividualRequestRepository extends JpaRepository<IndividualRequest, Long> {
    Collection<IndividualRequest> findBySsn(String ssn);
}
