package com.github.ferrantemattarutigliano.software.client.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.activity.LoginActivity;
import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;

public class LogoutFragment extends Fragment {

    public LogoutFragment() {
        // Required empty public constructor
    }

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SessionDirector.getStompClient().isConnected())
            SessionDirector.getStompClient().disconnect();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }

}
