package com.github.ferrantemattarutigliano.software.client.view;

import com.github.ferrantemattarutigliano.software.client.model.ReceivedRequestDTO;

import java.util.Collection;

public interface ManageRequestView {
    void onShowRequests(Collection<ReceivedRequestDTO> receivedRequestDTOS);
    void noRequestReceived();
    void onShowRequestsFail(String output);
    void onRequestHandleSuccess(String output);
    void onRequestHandleFail(String output);
}
