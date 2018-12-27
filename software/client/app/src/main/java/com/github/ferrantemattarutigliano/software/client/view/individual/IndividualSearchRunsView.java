package com.github.ferrantemattarutigliano.software.client.view.individual;

import com.github.ferrantemattarutigliano.software.client.model.RunDTO;

import java.util.Collection;

public interface IndividualSearchRunsView {
    void onRunFetch(Collection<RunDTO> output);
    void noAvailableRuns();
    void onRunInteraction(String message);
}
