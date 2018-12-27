package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.GroupRequest;
import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long> {
    Collection<GroupRequest> findByThirdParty(ThirdParty thirdParty);

    @Query("SELECT subscription FROM GroupRequest WHERE id = ?1")
    Boolean isSubscriptionRequest(Long id);

    @Query(value = "SELECT * FROM group_request WHERE subscription = TRUE", nativeQuery = true)
    Collection<GroupRequest> findSubscriptionRequest();
}
