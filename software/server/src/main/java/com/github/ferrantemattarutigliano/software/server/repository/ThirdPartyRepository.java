package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdPartyEntity;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.stream.Collectors;

@RepositoryRestResource
@Repository("ThirdParty")
public interface ThirdPartyRepository extends UserRepository<ThirdPartyEntity> {

    ThirdPartyEntity findByVat(String vat);

    @Override
    default boolean findUsername(String username) {
        Collection<ThirdPartyEntity> thirdParties;
        thirdParties = this.findAll().stream().collect(Collectors.toList());
        for(ThirdPartyEntity tp: thirdParties) {
            if (tp.getUsername().equals(username) )
                return true;
        }
        return false;
    }
}
