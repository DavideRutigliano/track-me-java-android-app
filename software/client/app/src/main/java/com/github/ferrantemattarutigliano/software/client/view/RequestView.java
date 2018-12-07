package com.github.ferrantemattarutigliano.software.client.view;

import com.github.ferrantemattarutigliano.software.client.model.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRequestDTO;

public interface RequestView {
    void onIndividualRequest(IndividualRequestDTO individualRequestDTO);
    void onGroupRequest(GroupRequestDTO groupRequestDTO);
    void onRequestSuccess(String output);
    void onRequestFail(String output);
}
