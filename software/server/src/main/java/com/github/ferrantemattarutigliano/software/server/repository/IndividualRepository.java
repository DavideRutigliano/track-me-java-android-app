package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualEntity;

public interface IndividualRepository extends UserRepository<IndividualEntity> {

    IndividualEntity findBySsn(String ssn);
}
