package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.HealthData;
import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.sql.Time;
import java.util.Collection;

public interface HealthDataRepository extends JpaRepository<HealthData, Long> {

    Collection<HealthData> findByIndividual(Individual individual);

    @Query(value = "SELECT * FROM health_data WHERE date <= ?2 AND time <= ?3 AND individual_id" +
            " = (SELECT id FROM individual WHERE ssn = ?1)", nativeQuery = true)
    Collection<HealthData> findUntilTimestamp(String ssn, Date date, Time time);

}
