package com.github.ferrantemattarutigliano.software.client.fragment.individual;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.activity.individual.IndividualCreatedRunsActivity;
import com.github.ferrantemattarutigliano.software.client.activity.individual.IndividualEnrolledRunsActivity;
import com.github.ferrantemattarutigliano.software.client.activity.individual.IndividualSearchRunsActivity;
import com.github.ferrantemattarutigliano.software.client.activity.individual.IndividualWatchedRunsActivity;

public class IndividualTrack4RunFragment extends Fragment {

    public IndividualTrack4RunFragment() {
        // Required empty public constructor
    }

    public static IndividualTrack4RunFragment newInstance() {
        IndividualTrack4RunFragment fragment = new IndividualTrack4RunFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_individual_track4run, container, false);
        Button createdRuns = v.findViewById(R.id.button_individual_created_runs);
        Button watchedRuns = v.findViewById(R.id.button_individual_watched_runs);
        Button enrolledRuns = v.findViewById(R.id.button_individual_enrolled_runs);
        Button searchRuns = v.findViewById(R.id.button_individual_search_runs);
        createdRuns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(IndividualCreatedRunsActivity.class);
            }
        });

        watchedRuns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(IndividualWatchedRunsActivity.class);
            }
        });

        enrolledRuns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(IndividualEnrolledRunsActivity.class);
            }
        });

        searchRuns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(IndividualSearchRunsActivity.class);
            }
        });
        return v;
    }

    private void changeActivity(Class destination){
        Intent intent = new Intent(getActivity(), destination);
        startActivity(intent);
    }

}
