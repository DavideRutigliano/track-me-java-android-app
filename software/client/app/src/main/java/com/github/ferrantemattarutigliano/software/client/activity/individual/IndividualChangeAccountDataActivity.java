package com.github.ferrantemattarutigliano.software.client.activity.individual;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.IndividualDTO;
import com.github.ferrantemattarutigliano.software.client.presenter.ChangeAccountDataPresenter;
import com.github.ferrantemattarutigliano.software.client.util.LoadingScreen;
import com.github.ferrantemattarutigliano.software.client.view.ChangeAccountDataView;

public class IndividualChangeAccountDataActivity extends AppCompatActivity implements ChangeAccountDataView {
    private AlertDialog.Builder dialogFactory;
    private ChangeAccountDataPresenter changeAccountDataPresenter;
    private LoadingScreen loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_change_account_data);
        changeAccountDataPresenter = new ChangeAccountDataPresenter(this);
        dialogFactory = new AlertDialog.Builder(this);
        ViewGroup layout = findViewById(R.id.layout_individual_change_account_data);
        loadingScreen = new LoadingScreen(layout, "Sending...");

        final Button changeAccountDataButton = findViewById(R.id.button_individual_change_account_data);
        final TextView firstNameText = findViewById(R.id.text_registration_individual_first_name);
        final TextView lastNametext = findViewById(R.id.text_registration_individual_last_name);
        final TextView stateText = findViewById(R.id.text_registration_individual_state);
        final TextView cityText = findViewById(R.id.text_registration_individual_city);
        final TextView addressText = findViewById(R.id.text_registration_individual_address);
        final TextView weightText = findViewById(R.id.text_registration_individual_weight);
        final TextView heightText = findViewById(R.id.text_registration_individual_height);

        changeAccountDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameText.getText().toString();
                String lastName = lastNametext.getText().toString();
                String state = stateText.getText().toString();
                String city = cityText.getText().toString();
                String address = addressText.getText().toString();
                int weight = 0;
                int height = 0;
                try{
                    weight = Integer.valueOf(weightText.getText().toString());
                    height = Integer.valueOf(heightText.getText().toString());
                }
                catch (NumberFormatException e) {
                    Log.w("MALFORMED_BIO", "Biometrics are empty or not numbers");
                }
                IndividualDTO individualDTO = new IndividualDTO();
                individualDTO.setFirstname(firstName);
                individualDTO.setLastname(lastName);
                individualDTO.setState(state);
                individualDTO.setCity(city);
                individualDTO.setAddress(address);
                individualDTO.setWeight(weight);
                individualDTO.setHeight(height);
                loadingScreen.show();
                changeAccountDataPresenter.doChangeIndividualAccountData(individualDTO);
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
