package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.Run;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RunRepository extends JpaRepository<Run, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Run SET state = 'started' WHERE id = ?1")
    void startRun(Long id);
}
