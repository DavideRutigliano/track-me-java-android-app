package com.github.ferrantemattarutigliano.software.client.fragment.individual;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.ReceivedRequestDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.ManageRequestPresenter;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.ManageRequestView;

import java.util.Collection;

public class IndividualManageRequestsFragment extends Fragment implements ManageRequestView {
    private ManageRequestPresenter manageRequestPresenter;
    private LinearLayout requestContainer;
    private LoadingScreen loadingScreen;

    public IndividualManageRequestsFragment() {
        // Required empty public constructor
    }

    public static IndividualManageRequestsFragment newInstance() {
        IndividualManageRequestsFragment fragment = new IndividualManageRequestsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manageRequestPresenter = new ManageRequestPresenter(this);
    }

    private void askForRequests(){
        loadingScreen.changeMessage("Fetching requests...");
        loadingScreen.show();
        manageRequestPresenter.getRequests();
    }

    private void handleRequest(ReceivedRequestDTO receivedRequestDTO){
        loadingScreen.changeMessage("Sending...");
        loadingScreen.show();
        manageRequestPresenter.handleRequest(receivedRequestDTO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_individual_manage_requests, container, false);
        requestContainer = v.findViewById(R.id.container_individual_manage_requests);
        ViewGroup layout = v.findViewById(R.id.layout_individual_manage_request);
        loadingScreen = new LoadingScreen(layout);
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
        getView().invalidate();
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
        manageRequestPresenter.getRequests();
    }

    @Override
    public void onRequestHandleFail(String output) {
        loadingScreen.hide();
    }
}
