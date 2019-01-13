package com.github.ferrantemattarutigliano.software.client.activity.thirdparty;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.fragment.LogoutFragment;
import com.github.ferrantemattarutigliano.software.client.fragment.NotImplementedFragment;
import com.github.ferrantemattarutigliano.software.client.fragment.thirdparty.ThirdPartyAccountFragment;
import com.github.ferrantemattarutigliano.software.client.fragment.thirdparty.ThirdPartyData4HelpFragment;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.thirdparty.ThirdPartyHomePresenter;
import com.github.ferrantemattarutigliano.software.client.session.Profile;
import com.github.ferrantemattarutigliano.software.client.session.SessionDirector;
import com.github.ferrantemattarutigliano.software.client.view.thirdparty.ThirdPartyHomeView;
import com.github.ferrantemattarutigliano.software.client.websocket.connection.StompCallback;
import com.github.ferrantemattarutigliano.software.client.websocket.connection.StompClient;
import com.github.ferrantemattarutigliano.software.client.websocket.payload.StompFrame;

import org.apache.commons.lang3.StringUtils;

public class ThirdPartyHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ThirdPartyHomeView {
    private ThirdPartyHomePresenter thirdPartyHomePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_third_party);
        thirdPartyHomePresenter = new ThirdPartyHomePresenter(this);
        thirdPartyHomePresenter.doFetchProfile();
    }

    @Override
    public void onProfileFetch(ThirdPartyDTO thirdPartyDTO) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create left navigation menu
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //populate profile with info
        String profileName = thirdPartyDTO.getOrganizationName();
        String profileInfo = thirdPartyDTO.getVat();
        Profile profile = new Profile(profileName, profileInfo);
        SessionDirector.setProfile(profile);

        //on start select the page my account
        changeShowedFragment(ThirdPartyAccountFragment.class);
        MenuItem myAccount = navigationView.getMenu().findItem(R.id.nav_my_account);
        selectItem(myAccount);

        String topic = "/notification/" + SessionDirector.USERNAME;
        SessionDirector.getStompClient().subscribe(topic);
        SharedPreferences sharedPreferences = getSharedPreferences("sub_" + SessionDirector.USERNAME, MODE_PRIVATE);
        SessionDirector.saveTopicSubscription(topic, sharedPreferences);
    }

    @Override
    public void startStompClient() {
        Intent intent = new Intent(this, ThirdPartyHomeActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        StompClient stompClient = new StompClient(new StompCallback() {
            @Override
            public void onResponseReceived(StompFrame response) {
                if (!response.getStompBody().isEmpty()) {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "trackme")
                            .setSmallIcon(R.drawable.trackme_small_logo)
                            .setContentTitle("Requests")
                            .setContentText(response.getStompBody())
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setContentIntent(pendingIntent);

                    if (Build.VERSION.SDK_INT < 26) {
                        notificationBuilder.setChannelId("trackme");
                    }
                    if (Build.VERSION.SDK_INT >= 26) { //push notifications require API level 26
                        NotificationChannel channel = new NotificationChannel(
                                "trackme",
                                "Requests",
                                NotificationManager.IMPORTANCE_DEFAULT
                        );
                        if (notificationManager != null) {
                            notificationManager.createNotificationChannel(channel);
                        }
                    }
                    notificationManager.notify(0, notificationBuilder.build());
                }
                if (response.getStompBody().contains("Request accepted")) {
                    String topic = StringUtils.substringBetween(response.getStompBody(), "Topic: ", ".");
                    SessionDirector.getStompClient().subscribe("/healthdata/" + topic);
                    SharedPreferences sharedPreferences = getSharedPreferences("sub_" + SessionDirector.USERNAME, MODE_PRIVATE);
                    SessionDirector.saveTopicSubscription("/healthdata/" + topic, sharedPreferences);
                }
            }
        });
        SessionDirector.setStompClient(stompClient);
        if (!SessionDirector.getStompClient().isConnected())
            SessionDirector.connect();
        SharedPreferences sharedPreferences = getSharedPreferences("sub_" + SessionDirector.USERNAME, MODE_PRIVATE);
        String[] topics = sharedPreferences.getString("topic", "").split(";");
        for (String topic : topics) {
            if (!topic.isEmpty())
                SessionDirector.getStompClient().subscribe(topic);
        }
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Class fragmentClass;
        switch (id){
            case R.id.nav_my_account:
                fragmentClass = ThirdPartyAccountFragment.class;
                break;
            case R.id.nav_data4help:
                fragmentClass = ThirdPartyData4HelpFragment.class;
                break;
            case R.id.nav_automatedsos:
                fragmentClass = NotImplementedFragment.class;
                break;
            case R.id.nav_logout:
                fragmentClass = LogoutFragment.class;
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
            int fragmentContainerId = R.id.frame_home_third_party_frag_container;
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(fragmentContainerId, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
