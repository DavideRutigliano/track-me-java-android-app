package com.github.ferrantemattarutigliano.software.client.activity.individual;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.fragment.NotImplementedFragment;
import com.github.ferrantemattarutigliano.software.client.fragment.individual.IndividualAccountFragment;

public class HomeIndividualActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_individual);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        changeShowedFragment(IndividualAccountFragment.class);
        MenuItem myAccount = navigationView.getMenu().findItem(R.id.nav_my_account);
        selectItem(myAccount);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_individual, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Class fragmentClass;
        switch (id){
            case R.id.nav_my_account:
                fragmentClass = IndividualAccountFragment.class;
                break;
            case R.id.nav_external_device:
                fragmentClass = NotImplementedFragment.class;
                break;
            case R.id.nav_manage_request:
                fragmentClass = NotImplementedFragment.class;
                break;
            case R.id.nav_automatedsos:
                fragmentClass = NotImplementedFragment.class;
                break;
            case R.id.nav_track4run:
                fragmentClass = NotImplementedFragment.class;
                break;
            case R.id.nav_logout:
                fragmentClass = NotImplementedFragment.class;
                break;
            default:
                fragmentClass = NotImplementedFragment.class;
        }
        changeShowedFragment(fragmentClass);
        selectItem(item);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectItem(MenuItem item){
        item.setChecked(true);
        setTitle(item.getTitle());
    }

    private void changeShowedFragment(Class fragmentClass){
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            int fragmentContainerId = R.id.frame_home_individual_frag_container;
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(fragmentContainerId, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
