package com.github.ferrantemattarutigliano.software.client.view.individual;

import com.github.ferrantemattarutigliano.software.client.model.RunDTO;

import java.util.Collection;

public interface IndividualWatchedRunsView {
    void onRunFetch(Collection<RunDTO> output);
    void noWatchedRuns();
    void onRunUnwatch(String message);
}
