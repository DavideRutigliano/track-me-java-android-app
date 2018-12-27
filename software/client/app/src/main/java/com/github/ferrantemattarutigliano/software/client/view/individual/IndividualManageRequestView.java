package com.github.ferrantemattarutigliano.software.client.view.individual;

import com.github.ferrantemattarutigliano.software.client.model.ReceivedRequestDTO;

import java.util.Collection;

public interface IndividualManageRequestView {
    void onShowRequests(Collection<ReceivedRequestDTO> receivedRequestDTOS);
    void noRequestReceived();
    void onShowRequestsFail(String output);
    void onRequestHandleSuccess(String output);
    void onRequestHandleFail(String output);
}
