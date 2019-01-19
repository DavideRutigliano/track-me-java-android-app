package com.github.ferrantemattarutigliano.software.client.view.thirdparty;

import com.github.ferrantemattarutigliano.software.client.model.HealthDataDTO;
import com.github.ferrantemattarutigliano.software.client.model.TaggedRequest;

import java.util.Collection;

public interface ThirdPartyViewData {

    void onRequestFetchSuccess(Collection<TaggedRequest> subscribedRequests);
    void noSentRequests(String output);
    void onDataFetchSuccess(Collection<HealthDataDTO> healthData);
}
