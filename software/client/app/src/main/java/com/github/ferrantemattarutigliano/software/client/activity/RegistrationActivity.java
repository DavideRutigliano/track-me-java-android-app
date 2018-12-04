package com.github.ferrantemattarutigliano.software.client.activity;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.ferrantemattarutigliano.software.client.Information;
import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.fragment.IndividualRegistrationFragment;
import com.github.ferrantemattarutigliano.software.client.fragment.ThirdPartyRegistrationFragment;
import com.github.ferrantemattarutigliano.software.client.presenter.RegistrationPresenter;
import com.github.ferrantemattarutigliano.software.client.view.RegistrationView;

public class RegistrationActivity extends AppCompatActivity
        implements RegistrationView, IndividualRegistrationFragment.OnFragmentInteractionListener {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private RegistrationPresenter registrationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = findViewById(R.id.container_toolbar_registration);
        setSupportActionBar(toolbar);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container_registration);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.container_tabs_registration);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    @Override
    public void onRegistrationSuccess(String output) {
    }

    @Override
    public void onRegistrationFail() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
