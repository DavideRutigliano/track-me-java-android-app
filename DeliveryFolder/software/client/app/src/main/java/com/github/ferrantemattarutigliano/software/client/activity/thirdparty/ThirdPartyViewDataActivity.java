package com.github.ferrantemattarutigliano.software.client.activity.thirdparty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
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
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //show back button on toolbar
        thirdPartyViewDataPresenter = new ThirdPartyViewDataPresenter(this);
        thirdPartyViewDataPresenter.doFetchSubscribedRequests();
        ViewGroup layout = findViewById(R.id.layout_third_party_view_data);
        loadingScreen = new LoadingScreen(layout, "Fetching data...");
        loadingScreen.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestFetchSuccess(Collection<TaggedRequest> sentRequests) {
        ViewGroup container = findViewById(R.id.container_third_party_sent_requests);
        LinearLayout stateBar = new LinearLayout(this);
        stateBar.setOrientation(LinearLayout.HORIZONTAL);

        final int TEXT_WIDTH = 300;

        //add title
        TextView titleText = new TextView(this);
        CharSequence title = "Title";
        titleText.setLayoutParams(new LinearLayout.LayoutParams(TEXT_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
        titleText.setText(title);
        stateBar.addView(titleText);
        addHorizontalSpace(stateBar);
        //add accepted
        TextView acceptText = new TextView(this);
        CharSequence accepted = "Accepted";
        acceptText.setText(accepted);
        stateBar.addView(acceptText);
        addHorizontalSpace(stateBar);
        //add subscription
        TextView subText= new TextView(this);
        CharSequence subscription = "Subscribed";
        subText.setText(subscription);
        stateBar.addView(subText);

        container.addView(stateBar);

        for(TaggedRequest request : sentRequests){
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            final RequestDTO requestDTO = request.getRequest();
            final String requestType = request.getType();
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(TEXT_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(requestDTO.toString());

            //if request is accepted and clicked go to the next screen
            if(requestDTO.getAccepted() != null && requestDTO.getAccepted()){
                textView.setClickable(true);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Long requestId = requestDTO.getId();
                        thirdPartyViewDataPresenter.doFetchHealthData(requestType, requestId);
                        loadingScreen.show();
                    }
                });
            }

            //add all icons
            linearLayout.addView(textView);
            addSpace(linearLayout, 110, 0);
            addIcon(linearLayout, requestDTO.getAccepted(), false);
            addSpace(linearLayout, 178, 0);
            addIcon(linearLayout, requestDTO.getSubscription(), true);

            container.addView(linearLayout);
            addVerticalSpace(container);
        }
        loadingScreen.hide();
    }

    private void addIcon(ViewGroup layout, Boolean isAccepted, boolean isTransparent){
        ImageView imageView = new ImageView(this);
        if(isAccepted == null){
            imageView.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
            imageView.setAlpha(0f);
        }
        else if(isAccepted){
            imageView.setImageDrawable(getResources().getDrawable(android.R.drawable.checkbox_on_background));
        }
        else{
            imageView.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
            if(isTransparent) imageView.setAlpha(0f);
        }
        layout.addView(imageView);
    }

    private void addHorizontalSpace(ViewGroup layout){
        addSpace(layout, 50, 0);
    }
    private void addVerticalSpace(ViewGroup layout){
        addSpace(layout, 0, 50);
    }

    private void addSpace(ViewGroup layout, int width, int height){
        Space horizontalSpace = new Space(this);
        horizontalSpace.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        layout.addView(horizontalSpace);
    }

    @Override
    public void noSentRequests(String output) {
        ViewGroup container = findViewById(R.id.container_third_party_sent_requests);
        container.removeAllViews();
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
        bundle.putSerializable("healthdata", (Serializable) healthData);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
