package com.github.ferrantemattarutigliano.software.client.fragment.thirdParty;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.activity.ChangeCredentialsActivity;
import com.github.ferrantemattarutigliano.software.client.session.Profile;
import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;

public class ThirdPartyAccountFragment extends Fragment {

    public ThirdPartyAccountFragment() {
        // Required empty public constructor
    }

    public static ThirdPartyAccountFragment newInstance() {
        ThirdPartyAccountFragment fragment = new ThirdPartyAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_my_third_party_account, container, false);
        TextView nameText = v.findViewById(R.id.text_third_party_account_name);
        TextView infoText = v.findViewById(R.id.text_third_party_account_vat);
        Profile profile = SessionDirector.getProfile();
        nameText.setText(profile.getName());
        infoText.setText(profile.getInfo());
        Button changeCredentialsButton = v.findViewById(R.id.button_third_party_account_change_credentials);
        changeCredentialsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangeCredentialsActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

}
