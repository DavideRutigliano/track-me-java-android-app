package com.github.ferrantemattarutigliano.software.client.model;

import java.util.Collection;

public class SentRequestDTO {
    private Collection<IndividualRequestDTO> individualRequestDTOS;
    private Collection<GroupRequestDTO> groupRequestDTOS;

    public SentRequestDTO() {
    }

    public Collection<IndividualRequestDTO> getIndividualRequestDTOS() {
        return individualRequestDTOS;
    }

    public void setIndividualRequestDTOS(Collection<IndividualRequestDTO> individualRequestDTOS) {
        this.individualRequestDTOS = individualRequestDTOS;
    }

    public Collection<GroupRequestDTO> getGroupRequestDTOS() {
        return groupRequestDTOS;
    }

    public void setGroupRequestDTOS(Collection<GroupRequestDTO> groupRequestDTOS) {
        this.groupRequestDTOS = groupRequestDTOS;
    }
}
