package com.github.ferrantemattarutigliano.software.client.fragment.individual;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.service.SendHealthDataService;


public class IndividualInsertDataFragment extends Fragment {

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

        final View view = inflater.inflate(R.layout.fragment_individual_insert_data, container, false);
        Button retryButton = view.findViewById(R.id.button_individual_connect_external_device);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reconnect(view);
            }
        });
        reconnect(view);
        return view;
    }


    private void reconnect(View v){
        TextView text = v.findViewById(R.id.text_individual_connect_external_device);
        CharSequence charSequence;
        if(SendHealthDataService.isDeviceConnected()){
            charSequence = "External device is correctly connected!";
        }
        else{
            charSequence = "External device is not connected";
        }
        text.setText(charSequence);
    }
}