package com.github.ferrantemattarutigliano.software.client.view;

import com.github.ferrantemattarutigliano.software.client.model.IndividualRequestDTO;

import java.util.Collection;

public interface ManageRequestView {
    void onShowRequests(Collection<IndividualRequestDTO> individualRequestDTOS);
    void noRequestReceived();
    void onShowRequestsFail(String output);
}
