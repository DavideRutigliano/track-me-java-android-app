package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.IndividualEntity;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.stream.Collectors;

@RepositoryRestResource
@Repository("Individual")
public interface IndividualRepository extends UserRepository<IndividualEntity> {

    IndividualEntity findBySsn(String ssn);

    @Override
    default boolean findUsername(String username) {
        Collection<IndividualEntity> individuals;
        individuals = this.findAll().stream().collect(Collectors.toList());
        for(IndividualEntity i: individuals) {
            if ( i.getUsername().equals(username) )
                return true;
        }
        return false;
    }
}
