package com.github.ferrantemattarutigliano.software.client.view;

import com.github.ferrantemattarutigliano.software.client.model.RunDTO;

import java.util.Collection;

public interface IndividualEnrolledRunsView {
    void onRunFetch(Collection<RunDTO> output);
    void noEnrolledRuns();
    void onRunUnroll(String message);
}
