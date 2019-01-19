package com.github.ferrantemattarutigliano.software.client.fragment.individual;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.ReceivedRequestDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.individual.IndividualManageRequestPresenter;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualManageRequestView;

import java.util.Collection;

public class IndividualManageRequestsFragment extends Fragment implements IndividualManageRequestView {
    private IndividualManageRequestPresenter individualManageRequestPresenter;
    private LinearLayout requestContainer;
    private LoadingScreen loadingScreen;

    public IndividualManageRequestsFragment() {
        // Required empty public constructor
    }

    public static IndividualManageRequestsFragment newInstance() {
        return new IndividualManageRequestsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        individualManageRequestPresenter = new IndividualManageRequestPresenter(this);
    }

    private void askForRequests(){
        loadingScreen.changeMessage("Fetching requests...");
        loadingScreen.show();
        individualManageRequestPresenter.getRequests();
    }

    private void handleRequest(ReceivedRequestDTO receivedRequestDTO){
        loadingScreen.changeMessage("Sending...");
        loadingScreen.show();
        individualManageRequestPresenter.handleRequest(receivedRequestDTO);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_individual_manage_requests, container, false);
        requestContainer = v.findViewById(R.id.container_individual_manage_requests);
        ViewGroup layout = v.findViewById(R.id.layout_individual_manage_request);
        loadingScreen = new LoadingScreen(layout);
        Button oldRequestsButton = v.findViewById(R.id.button_individual_view_old_requests);
        oldRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Not implemented", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        askForRequests();
        return v;
    }

    @Override
    public void onShowRequests(Collection<ReceivedRequestDTO> receivedRequestDTOS) {
        requestContainer.removeAllViews();
        for(final ReceivedRequestDTO request : receivedRequestDTOS){
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            //request text
            TextView requestText = new TextView(getContext());
            String requestString = request.toString();
            requestText.setText(requestString);
            //request buttons
            Button acceptButton = new Button(getContext());
            CharSequence acceptText = "Accept";
            acceptButton.setText(acceptText);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    request.setAccepted(true);
                    handleRequest(request);
                }
            });
            Button rejectButton = new Button(getContext());
            CharSequence rejectText = "Reject";
            rejectButton.setText(rejectText);
            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    request.setAccepted(false);
                    handleRequest(request);
                }
            });

            linearLayout.addView(requestText);
            linearLayout.addView(acceptButton);
            linearLayout.addView(rejectButton);
            requestContainer.addView(linearLayout);
        }
        //getView().invalidate();
        loadingScreen.hide();
    }

    @Override
    public void noRequestReceived() {
        requestContainer.removeAllViews();
        TextView textView = new TextView(getContext());
        CharSequence text = "No request received yet!";
        textView.setText(text);
        textView.setTextSize(20);
        requestContainer.addView(textView);
        loadingScreen.hide();
    }

    @Override
    public void onShowRequestsFail(String output) {
        loadingScreen.hide();
    }

    @Override
    public void onRequestHandleSuccess(String output) {
        individualManageRequestPresenter.getRequests();
    }

    @Override
    public void onRequestHandleFail(String output) {
        loadingScreen.hide();
    }
}
