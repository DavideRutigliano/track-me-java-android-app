package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.HealthData;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface HealthDataRepository extends JpaRepository<HealthData, Long> {
    //TODO a find by criteria method 

}
