package com.github.ferrantemattarutigliano.software.client.activity.thirdparty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.HealthDataDTO;
import com.github.ferrantemattarutigliano.software.client.model.RequestDTO;
import com.github.ferrantemattarutigliano.software.client.model.TaggedRequest;
import com.github.ferrantemattarutigliano.software.client.presenter.thirdparty.ThirdPartyViewDataPresenter;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.thirdparty.ThirdPartyViewData;

import java.io.Serializable;
import java.util.Collection;

public class ThirdPartyViewDataActivity extends AppCompatActivity implements ThirdPartyViewData {
    private ThirdPartyViewDataPresenter thirdPartyViewDataPresenter;
    private LoadingScreen loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_view_data);
        thirdPartyViewDataPresenter = new ThirdPartyViewDataPresenter(this);
        thirdPartyViewDataPresenter.doFetchSubscribedRequests();
        ViewGroup layout = findViewById(R.id.layout_third_party_view_data);
        loadingScreen = new LoadingScreen(layout, "Fetching data...");
        loadingScreen.show();
    }

    @Override
    public void onRequestFetchSuccess(Collection<TaggedRequest> subscribedRequests) {
        ViewGroup container = findViewById(R.id.container_subscribed_requests);

        for(TaggedRequest request : subscribedRequests){
            final RequestDTO requestDTO = request.getRequest();
            final String requestType = request.getType();
            TextView textView = new TextView(this);
            textView.setText(requestDTO.toString());
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Long requestId = requestDTO.getId();
                    thirdPartyViewDataPresenter.doFetchHealthData(requestType, requestId);
                    loadingScreen.show();
                }
            });
            container.addView(textView);
        }
        loadingScreen.hide();
    }

    @Override
    public void noSubscribedRequests(String output) {
        ViewGroup container = findViewById(R.id.container_subscribed_requests);
        CharSequence text = "No subscribed requests!";
        TextView textView = new TextView(this);
        textView.setText(text);
        container.addView(textView);
        loadingScreen.hide();
    }

    @Override
    public void onDataFetchSuccess(Collection<HealthDataDTO> healthData) {
        loadingScreen.hide();
        Intent intent = new Intent(ThirdPartyViewDataActivity.this, ThirdPartyHealthDataActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("healthdata", (Serializable)healthData);
        startActivity(intent);
    }
}
