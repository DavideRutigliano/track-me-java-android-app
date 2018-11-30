package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualRepository extends UserRepository<Individual> {

    Individual findBySsn(String ssn);
    Boolean ssnAlreadyExists(String ssn);
}
