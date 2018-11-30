package com.github.ferrantemattarutigliano.software.server.repository;

import com.github.ferrantemattarutigliano.software.server.model.entity.ThirdParty;

public interface ThirdPartyRepository extends UserRepository<ThirdParty> {
    ThirdParty findByVat(String vat);
}
