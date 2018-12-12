package com.github.ferrantemattarutigliano.software.client.activity.thirdparty;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.Information;
import com.github.ferrantemattarutigliano.software.client.LoadingViewFactory;
import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.fragment.thirdParty.ThirdPartyGroupRequestFragment;
import com.github.ferrantemattarutigliano.software.client.fragment.thirdParty.ThirdPartyIndividualRequestFragment;
import com.github.ferrantemattarutigliano.software.client.model.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRequestDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.RequestPresenter;
import com.github.ferrantemattarutigliano.software.client.view.RequestView;

public class ThirdPartyRequestActivity extends AppCompatActivity implements RequestView {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private RequestPresenter requestPresenter;
    private LinearLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_request);
        Toolbar toolbar = findViewById(R.id.container_toolbar_request);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container_tab_request);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.container_tabs_request);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        requestPresenter = new RequestPresenter(this);
    }

    @Override
    public void onIndividualRequest(IndividualRequestDTO individualRequestDTO) {
        createLoadingScreen();
        requestPresenter.doIndividualRequest(individualRequestDTO);
    }

    @Override
    public void onGroupRequest(GroupRequestDTO groupRequestDTO) {
        createLoadingScreen();
        requestPresenter.doGroupRequest(groupRequestDTO);
    }

    private void createLoadingScreen(){
        LoadingViewFactory loadingViewFactory = new LoadingViewFactory();
        loadingLayout = loadingViewFactory.create(this, "Sending...");
        CoordinatorLayout container = findViewById(R.id.container_third_party_request);
        container.addView(loadingLayout);
    }

    private void deleteLoadingScreen(){
        CoordinatorLayout container = findViewById(R.id.container_third_party_request);
        container.removeView(loadingLayout);
    }

    @Override
    public void onRequestSuccess(String output) {
        deleteLoadingScreen();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); //close the activity after a request has been made
            }
        });
        alertDialogBuilder.setTitle("Request sent successfully");
        alertDialogBuilder.setMessage(output);
        alertDialogBuilder.show();
    }

    @Override
    public void onRequestFail(String output) {
        deleteLoadingScreen();
        Toast.makeText(this, output, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //this inner class returns the corresponding fragment for each tab
    //i.e individualReq-Fragment and groupReq-Fragment
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return ThirdPartyIndividualRequestFragment.newInstance();
                case 1:
                    return ThirdPartyGroupRequestFragment.newInstance();
            }
            throw new RuntimeException(Information.MISSING_TAB.toString());
        }

        @Override
        public int getCount() {
            return 2; //how many tabs
        }
    }
}
