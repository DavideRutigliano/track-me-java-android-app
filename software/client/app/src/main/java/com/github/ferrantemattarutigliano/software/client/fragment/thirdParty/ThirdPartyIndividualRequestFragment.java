package com.github.ferrantemattarutigliano.software.client.fragment.thirdparty;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.client.view.thirdparty.ThirdPartyRequestView;

public class ThirdPartyIndividualRequestFragment extends Fragment{
    private ThirdPartyRequestView thirdPartyRequestView;

    public ThirdPartyIndividualRequestFragment() {}

    public static ThirdPartyIndividualRequestFragment newInstance() {
        ThirdPartyIndividualRequestFragment fragment = new ThirdPartyIndividualRequestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third_party_individual_request, container, false);
        final Button confirmButton = v.findViewById(R.id.button_individual_request_send);
        final TextView ssnText = v.findViewById(R.id.text_individual_request_ssn);
        final CheckBox subscribeCheck = v.findViewById(R.id.check_individual_request_subscribe);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssn = ssnText.getText().toString();
                boolean subscribe = subscribeCheck.isChecked();
                IndividualRequestDTO request = new IndividualRequestDTO(ssn, subscribe);
                thirdPartyRequestView.onIndividualRequest(request);
            }
        });
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ThirdPartyRequestView) {
            thirdPartyRequestView = (ThirdPartyRequestView) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ThirdPartyRequestView");
        }
    }
}
