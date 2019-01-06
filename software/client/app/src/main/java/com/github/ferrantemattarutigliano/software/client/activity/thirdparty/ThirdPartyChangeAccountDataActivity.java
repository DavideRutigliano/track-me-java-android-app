package com.github.ferrantemattarutigliano.software.client.activity.thirdparty;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.ChangeAccountDataPresenter;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.ChangeAccountDataView;

public class ThirdPartyChangeAccountDataActivity extends AppCompatActivity implements ChangeAccountDataView {
    private AlertDialog.Builder dialogFactory;
    private ChangeAccountDataPresenter changeAccountDataPresenter;
    private LoadingScreen loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_change_account_data);
        changeAccountDataPresenter = new ChangeAccountDataPresenter(this);
        dialogFactory = new AlertDialog.Builder(this);
        ViewGroup layout = findViewById(R.id.layout_third_party_change_account_data);
        loadingScreen = new LoadingScreen(layout, "Sending...");

        final Button changeAccountDataButton = findViewById(R.id.button_third_party_account_change_account_data);
        final TextView orgName = findViewById(R.id.text_registration_third_party_name);

        changeAccountDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = orgName.getText().toString();
                ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO();
                thirdPartyDTO.setOrganizationName(name);
                loadingScreen.show();
                changeAccountDataPresenter.doChangeThirdPartyAccountData(thirdPartyDTO);
            }
        });
    }

    @Override
    public void onProfileUpdateSuccess(String output) {
        loadingScreen.hide();
        Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG)
                .show();
        finish();
    }

    @Override
    public void onProfileUpdateFail(String output) {
        loadingScreen.hide();
        dialogFactory.setTitle("Account data information")
                .setMessage(output)
                .setPositiveButton("Okay", null)
                .show();

    }
}
