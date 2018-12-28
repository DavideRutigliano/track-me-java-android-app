package com.github.ferrantemattarutigliano.software.client.fragment.thirdparty;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.activity.thirdparty.ThirdPartyThirdPartyRequestActivity;
import com.github.ferrantemattarutigliano.software.client.activity.thirdparty.ThirdPartyViewDataActivity;

public class ThirdPartyData4HelpFragment extends Fragment {

    public ThirdPartyData4HelpFragment() {
        // Required empty public constructor
    }

    public static ThirdPartyData4HelpFragment newInstance() {
        return new ThirdPartyData4HelpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third_party_data4help, container, false);
        Button requestButton = v.findViewById(R.id.button_third_party_new_request);
        Button viewData = v.findViewById(R.id.button_third_party_view_data);

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThirdPartyThirdPartyRequestActivity.class);
                startActivity(intent);
            }
        });

        viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThirdPartyViewDataActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

}
