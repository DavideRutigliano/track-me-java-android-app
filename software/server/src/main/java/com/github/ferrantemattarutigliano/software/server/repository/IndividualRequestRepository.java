package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.GroupRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface IndividualRequestRepository extends JpaRepository<IndividualRequest, Long> {
    Collection<IndividualRequest> findByThirdParty(Long id);
    Collection<IndividualRequest> findBySsn(String ssn);

    @Modifying
    @Query("UPDATE IndividualRequest SET accepted = ?2 WHERE id = ?1")
    Boolean handleRequest(Long id, Boolean accepted);
}
