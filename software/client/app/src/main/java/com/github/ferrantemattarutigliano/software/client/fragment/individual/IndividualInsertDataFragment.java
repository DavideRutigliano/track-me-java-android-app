package com.github.ferrantemattarutigliano.software.client.fragment.individual;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.presenter.individual.IndividualInsertDataPresenter;
import com.github.ferrantemattarutigliano.software.client.view.individual.IndividualInsertDataView;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;


public class IndividualInsertDataFragment extends Fragment implements
        IndividualInsertDataView,
        DataClient.OnDataChangedListener {

    private IndividualInsertDataPresenter individualInsertDataPresenter;
    private TextView messageContainer;
    String datapath = "/data_path";
    private Button deviceButton;

    public IndividualInsertDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        individualInsertDataPresenter = new IndividualInsertDataPresenter(this);
        Wearable.getDataClient(getContext()).addListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        individualInsertDataPresenter = new IndividualInsertDataPresenter(this);

        View v = inflater.inflate(R.layout.fragment_individual_connect_external_device, container, false);
        messageContainer = v.findViewById(R.id.container_individual_external_device_messages);
        deviceButton = v.findViewById(R.id.button_individual_connect_external_device);

        Wearable.getDataClient(getContext()).addListener(this);

        deviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messages = messageContainer.getText().toString();
                individualInsertDataPresenter.doInsertData(messages);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Wearable.getDataClient(getContext()).addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Wearable.getDataClient(getContext()).removeListener(this);
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (datapath.equals(path)) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                    String message = dataMapItem.getDataMap().getString("message");
                    messageContainer.append(message + "\n");
                }
            }
        }
    }

    @Override
    public void onInsertDataSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        messageContainer.setText("");
    }

    @Override
    public void onInsertDataFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        messageContainer.setText("");
    }
}