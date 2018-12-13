package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.HealthData;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.Collection;

public interface HealthDataRepository extends JpaRepository<HealthData, Long> {

    Collection<HealthData> findByIndividual(Individual individual);

    @Query(value = "SELECT * FROM HealthData WHERE timestamp <= ?1", nativeQuery = true)
    Collection<HealthData> findUntilTimestamp(Date timestamp);

}
