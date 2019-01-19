package com.github.ferrantemattarutigliano.software.client.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.fragment.individual.IndividualRegistrationFragment;
import com.github.ferrantemattarutigliano.software.client.fragment.thirdparty.ThirdPartyRegistrationFragment;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRegistrationDTO;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyRegistrationDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.RegistrationPresenter;
import com.github.ferrantemattarutigliano.software.client.util.Information;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.RegistrationView;

public class RegistrationActivity extends AppCompatActivity implements RegistrationView {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private RegistrationPresenter registrationPresenter;
    private LoadingScreen loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        CoordinatorLayout layout = findViewById(R.id.layout_registration);
        Toolbar toolbar = findViewById(R.id.container_toolbar_registration);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container_registration);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.container_tabs_registration);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        registrationPresenter = new RegistrationPresenter(this);
        loadingScreen = new LoadingScreen(layout, "Sending...");
    }

    @Override
    public void onRegistrationSuccess(String output) {
        loadingScreen.hide();
        Toast.makeText(getBaseContext(), output, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onRegistrationFail(String output) {
        loadingScreen.hide();
        Toast.makeText(getBaseContext(), output, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIndividualRegistration(IndividualRegistrationDTO individualRegistrationDTO) {
        loadingScreen.show();
        registrationPresenter.doIndividualRegistration(individualRegistrationDTO);
    }

    @Override
    public void onThirdPartyRegistration(ThirdPartyRegistrationDTO thirdPartyRegistrationDTO) {
        loadingScreen.show();
        registrationPresenter.doThirdPartyRegistration(thirdPartyRegistrationDTO);
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
    //i.e individualReg-Fragment and thirdPartyReg-Fragment
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return IndividualRegistrationFragment.newInstance();
                case 1:
                    return ThirdPartyRegistrationFragment.newInstance();
            }
            throw new RuntimeException(Information.MISSING_TAB.toString());
        }

        @Override
        public int getCount() {
            return 2; //how many tabs
        }
    }
}
