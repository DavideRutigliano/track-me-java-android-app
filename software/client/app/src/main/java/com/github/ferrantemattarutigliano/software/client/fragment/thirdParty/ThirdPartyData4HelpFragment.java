package com.github.ferrantemattarutigliano.software.client.fragment.thirdParty;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ferrantemattarutigliano.software.client.R;

public class ThirdPartyData4HelpFragment extends Fragment {

    public ThirdPartyData4HelpFragment() {
        // Required empty public constructor
    }

    public static ThirdPartyData4HelpFragment newInstance() {
        ThirdPartyData4HelpFragment fragment = new ThirdPartyData4HelpFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_third_party_account, container, false);
    }

}
