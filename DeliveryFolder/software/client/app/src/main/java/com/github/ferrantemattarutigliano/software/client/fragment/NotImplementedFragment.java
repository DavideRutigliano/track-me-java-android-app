package com.github.ferrantemattarutigliano.software.client.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ferrantemattarutigliano.software.client.R;

public class NotImplementedFragment extends Fragment {

    public NotImplementedFragment() {
        // Required empty public constructor
    }

    public static NotImplementedFragment newInstance() {
        NotImplementedFragment fragment = new NotImplementedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_not_implemented, container, false);
    }

}
