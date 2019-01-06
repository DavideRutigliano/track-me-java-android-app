package com.github.ferrantemattarutigliano.software.client.fragment.individual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.service.SendHealthDataService;


public class IndividualInsertDataFragment extends Fragment {

    private Button deviceButton;

    private SendHealthDataService sendHealthDataService;
    private Intent sendHealthDataIntent;

    public IndividualInsertDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sendHealthDataService = new SendHealthDataService();
        sendHealthDataIntent = new Intent(getActivity(), sendHealthDataService.getClass());

        View v = inflater.inflate(R.layout.fragment_individual_insert_data, container, false);
        deviceButton = v.findViewById(R.id.button_individual_send_data);

        deviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startService(sendHealthDataIntent);
            }
        });

        return v;
    }
}