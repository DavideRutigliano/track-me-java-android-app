package com.github.ferrantemattarutigliano.software.client.fragment.individual;


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
import com.github.ferrantemattarutigliano.software.client.activity.LoginActivity;
import com.github.ferrantemattarutigliano.software.client.activity.MainActivity;
import com.github.ferrantemattarutigliano.software.client.session.Profile;
import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;

public class IndividualAccountFragment extends Fragment {

    public IndividualAccountFragment() {
        // Required empty public constructor
    }

    public static IndividualAccountFragment newInstance() {
        IndividualAccountFragment fragment = new IndividualAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_my_individual_account, container, false);
        TextView nameText = v.findViewById(R.id.text_individual_account_name);
        TextView infoText = v.findViewById(R.id.text_individual_account_ssn);
        Profile profile = SessionDirector.getProfile();
        nameText.setText(profile.getName());
        infoText.setText(profile.getInfo());
        Button changeCredentialsButton = v.findViewById(R.id.button_individual_account_change_credentials);
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
