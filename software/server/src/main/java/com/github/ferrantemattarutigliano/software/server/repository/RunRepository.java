package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.Individual;
import com.github.ferrantemattarutigliano.software.server.model.entity.Run;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RunRepository extends JpaRepository<Run, Long> {
}
