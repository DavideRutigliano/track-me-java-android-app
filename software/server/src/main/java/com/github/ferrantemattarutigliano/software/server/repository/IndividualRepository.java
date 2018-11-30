package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;

public interface IndividualRepository extends UserRepository<Individual> {
    Individual findBySsn(String ssn);
    Boolean existsBySsn(String ssn);
}
