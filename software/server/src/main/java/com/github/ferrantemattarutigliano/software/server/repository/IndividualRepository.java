package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IndividualRepository<I extends Individual> extends JpaRepository<I, Long>, JpaSpecificationExecutor<Individual> {
    Boolean existsByUser(User user);
    Individual findByUser(User user);
    Individual findBySsn(String ssn);
    Boolean existsBySsn(String ssn);
}
