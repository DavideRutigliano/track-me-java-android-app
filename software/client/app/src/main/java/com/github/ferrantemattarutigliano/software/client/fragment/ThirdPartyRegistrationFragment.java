package com.github.ferrantemattarutigliano.software.client.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ferrantemattarutigliano.software.client.R;

public class ThirdPartyRegistrationFragment extends Fragment {

    public ThirdPartyRegistrationFragment() {}

    public static ThirdPartyRegistrationFragment newInstance() {
        ThirdPartyRegistrationFragment fragment = new ThirdPartyRegistrationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_third_party_registration, container, false);
    }
}
