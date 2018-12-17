package com.github.ferrantemattarutigliano.software.client.view;

public interface IndividualNewRunView {
    void pathTooShort();
    void onCreateRunSuccess(String message);
    void onCreateRunFail(String message);
}
