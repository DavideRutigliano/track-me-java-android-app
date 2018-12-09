package com.github.ferrantemattarutigliano.software.client.fragment.thirdParty;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyDTO;
import com.github.ferrantemattarutigliano.software.client.model.ThirdPartyRegistrationDTO;
import com.github.ferrantemattarutigliano.software.client.model.UserDTO;
import com.github.ferrantemattarutigliano.software.client.view.RegistrationView;

public class ThirdPartyRegistrationFragment extends Fragment{
    private RegistrationView registrationView;

    public ThirdPartyRegistrationFragment() {}

    public static ThirdPartyRegistrationFragment newInstance() {
        ThirdPartyRegistrationFragment fragment = new ThirdPartyRegistrationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third_party_registration, container, false);
        final Button signUpButton = v.findViewById(R.id.button_third_party_register);
        final TextView usernameText = v.findViewById(R.id.text_registration_third_party_username);
        final TextView passwordText = v.findViewById(R.id.text_registration_third_party_password);
        final TextView repeatPasswordText = v.findViewById(R.id.text_registration_third_party_repeat_password);
        final TextView emailText = v.findViewById(R.id.text_registration_third_party_email);
        final TextView orgName = v.findViewById(R.id.text_registration_third_party_name);
        final TextView vatText = v.findViewById(R.id.text_registration_third_party_vat);

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
                String name = orgName.getText().toString();
                String vat = vatText.getText().toString();
                UserDTO userDTO = new UserDTO(username, password, email, "THIRD_PARTY");
                ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO(vat, name);
                ThirdPartyRegistrationDTO registrationDTO = new ThirdPartyRegistrationDTO(userDTO, thirdPartyDTO);
                registrationView.onThirdPartyRegistration(registrationDTO);
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
