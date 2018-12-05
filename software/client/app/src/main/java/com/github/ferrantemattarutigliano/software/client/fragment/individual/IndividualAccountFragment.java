package com.github.ferrantemattarutigliano.software.client.fragment.individual;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ferrantemattarutigliano.software.client.R;

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
        return inflater.inflate(R.layout.fragment_my_individual_account, container, false);
    }

}
