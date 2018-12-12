package com.github.ferrantemattarutigliano.software.client.fragment.individual;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.IndividualDTO;
import com.github.ferrantemattarutigliano.software.client.model.IndividualRegistrationDTO;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;
import com.github.ferrantemattarutigliano.software.client.view.RegistrationView;

public class IndividualRegistrationFragment extends Fragment{
    private RegistrationView registrationView;

    public IndividualRegistrationFragment() {}

    public static IndividualRegistrationFragment newInstance() {
        IndividualRegistrationFragment fragment = new IndividualRegistrationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_individual_registration, container, false);
        final Button signUpButton = v.findViewById(R.id.button_individual_register);
        final TextView usernameText = v.findViewById(R.id.text_registration_individual_username);
        final TextView passwordText = v.findViewById(R.id.text_registration_individual_password);
        final TextView repeatPasswordText = v.findViewById(R.id.text_registration_individual_repeat_password);
        final TextView emailText = v.findViewById(R.id.text_registration_individual_email);
        final TextView firstNameText = v.findViewById(R.id.text_registration_individual_first_name);
        final TextView lastNametext = v.findViewById(R.id.text_registration_individual_last_name);
        final TextView ssnText = v.findViewById(R.id.text_registration_individual_ssn);
        final TextView birthdateText = v.findViewById(R.id.text_registration_individual_birthdate);
        final TextView stateText = v.findViewById(R.id.text_registration_individual_state);
        final TextView cityText = v.findViewById(R.id.text_registration_individual_city);
        final TextView addressText = v.findViewById(R.id.text_registration_individual_address);
        final TextView weightText = v.findViewById(R.id.text_registration_individual_weight);
        final TextView heightText = v.findViewById(R.id.text_registration_individual_height);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordText.getText().toString();
                String repeatPassword = repeatPasswordText.getText().toString();
                if(!password.equals(repeatPassword)) {
                    Toast.makeText(getActivity(), "Password don't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                String username = usernameText.getText().toString();
                String email = emailText.getText().toString();
                String firstName = firstNameText.getText().toString();
                String lastName = lastNametext.getText().toString();
                String ssn = ssnText.getText().toString();
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
                //todo add other attributes (eg birthdate)
                UserDTO userDTO = new UserDTO(username, password, email, "INDIVIDUAL");
                IndividualDTO individualDTO = new IndividualDTO();
                individualDTO.setSsn(ssn);
                individualDTO.setFirstname(firstName);
                individualDTO.setLastname(lastName);
                individualDTO.setState(state);
                individualDTO.setCity(city);
                individualDTO.setAddress(address);
                individualDTO.setWeight(weight);
                individualDTO.setHeight(height);
                IndividualRegistrationDTO registrationDTO = new IndividualRegistrationDTO(userDTO, individualDTO);
                registrationView.onIndividualRegistration(registrationDTO);
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationView) {
            registrationView = (RegistrationView) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegistrationView");
        }
    }
}
