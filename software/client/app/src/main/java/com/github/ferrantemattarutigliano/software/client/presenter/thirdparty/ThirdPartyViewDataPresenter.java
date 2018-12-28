package com.github.ferrantemattarutigliano.software.client.presenter.thirdparty;

import com.github.ferrantemattarutigliano.software.client.httprequest.AsyncResponse;
import com.github.ferrantemattarutigliano.software.client.model.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.client.model.HealthDataDTO;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.client.model.SentRequestDTO;
import com.github.ferrantemattarutigliano.software.client.model.TaggedRequest;
import com.github.ferrantemattarutigliano.software.client.presenter.Presenter;
import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;
import com.github.ferrantemattarutigliano.software.client.task.thirdparty.ThirdPartyGetHealthDataTask;
import com.github.ferrantemattarutigliano.software.client.task.thirdparty.ThirdPartySentRequestsTask;
import com.github.ferrantemattarutigliano.software.client.view.thirdparty.ThirdPartyViewData;

import java.util.Collection;
import java.util.LinkedHashSet;

public class ThirdPartyViewDataPresenter extends Presenter<ThirdPartyViewData> {
    public ThirdPartyViewDataPresenter(ThirdPartyViewData view) {
        super(view);
    }

    public void doFetchSubscribedRequests(){
        new ThirdPartySentRequestsTask(SessionDirector.USERNAME, "/all", new AsyncResponse<SentRequestDTO>() {
            @Override
            public void taskFinish(SentRequestDTO output) {
                if(output.getGroupRequestDTOS().isEmpty()
                        && output.getIndividualRequestDTOS().isEmpty()){
                    view.noSentRequests("No subscribed requests found.");
                    return;
                }
                Collection<TaggedRequest> requests = new LinkedHashSet<>();
                for(IndividualRequestDTO Ireq : output.getIndividualRequestDTOS()){
                    TaggedRequest<IndividualRequestDTO> request = new TaggedRequest<>("Individual", Ireq);
                    requests.add(request);
                }
                for(GroupRequestDTO Greq : output.getGroupRequestDTOS()){
                    TaggedRequest<GroupRequestDTO> request = new TaggedRequest<>("Group", Greq);
                    requests.add(request);
                }
                view.onRequestFetchSuccess(requests);
            }

            @Override
            public void taskFailMessage(String message) {
            }
        }).execute();
    }

    public void doFetchHealthData(String requestType, Long requestId){
        new ThirdPartyGetHealthDataTask(requestType, requestId, new AsyncResponse<Collection<HealthDataDTO>>() {
            @Override
            public void taskFinish(Collection<HealthDataDTO> output) {
                view.onDataFetchSuccess(output);
            }

            @Override
            public void taskFailMessage(String message) {
            }
        }).execute();
    }
}
