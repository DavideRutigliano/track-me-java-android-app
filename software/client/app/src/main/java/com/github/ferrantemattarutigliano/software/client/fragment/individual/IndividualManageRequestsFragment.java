package com.github.ferrantemattarutigliano.software.client.fragment.individual;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.ManageRequestPresenter;
import com.github.ferrantemattarutigliano.software.client.view.ManageRequestView;

import java.util.Collection;

public class IndividualManageRequestsFragment extends Fragment implements ManageRequestView {
    private ManageRequestPresenter manageRequestPresenter;

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
        manageRequestPresenter.getRequests();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_individual_manage_requests, container, false);
    }

    @Override
    public void onShowRequests(Collection<IndividualRequestDTO> individualRequestDTOS) {
        LinearLayout requestContainer = getView().findViewById(R.id.container_individual_manage_requests);
        requestContainer.removeAllViews(); //clear all requests
        for(IndividualRequestDTO request : individualRequestDTOS){
            TextView textView = new TextView(getContext());
            textView.setText(request.toString());
            requestContainer.addView(textView);
        }
    }

    @Override
    public void noRequestReceived() {

    }

    @Override
    public void onShowRequestsFail(String output) {

    }
}
