package com.github.ferrantemattarutigliano.software.client.fragment.thirdparty;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.ferrantemattarutigliano.software.client.R;
import com.github.ferrantemattarutigliano.software.client.model.GroupRequestDTO;
import com.github.ferrantemattarutigliano.software.client.view.thirdparty.ThirdPartyRequestView;

import java.util.ArrayList;
import java.util.List;

//TODO finish implementation for birthdate, height and weight
public class ThirdPartyGroupRequestFragment extends Fragment{
    private ThirdPartyRequestView thirdPartyRequestView;

    public ThirdPartyGroupRequestFragment() {}

    public static ThirdPartyGroupRequestFragment newInstance() {
        return new ThirdPartyGroupRequestFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_third_party_group_request, container, false);
        final Button confirmButton = v.findViewById(R.id.button_group_request_send);
        final CheckBox subscribeCheck = v.findViewById(R.id.check_group_request_subscribe);
        final Spinner criteriaSpinner = v.findViewById(R.id.spinner_group_request_criteria);
        final FloatingActionButton addCriteriaButton = v.findViewById(R.id.button_group_request_add_criterion);
        final LinearLayout criteriaContainer = v.findViewById(R.id.container_group_request_criteria);
        final AlertDialog.Builder dialogFactory = new AlertDialog.Builder(getContext());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.group_request_criteria, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        criteriaSpinner.setAdapter(adapter);

        addCriteriaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                String item = criteriaSpinner.getSelectedItem().toString();
                switch (item) {
                    case "Firstname":
                        final TextView textFirstname = new TextView(getContext());
                        textFirstname.setText("Firstname: ");
                        final EditText firstname = new EditText(getContext());
                        firstname.setLayoutParams(new LinearLayout.LayoutParams(500,ViewGroup.LayoutParams.WRAP_CONTENT));
                        CheckBox searchInWordsFirstname = new CheckBox(getContext());
                        CharSequence searchInWordsFirstnameText = "Search in words";
                        searchInWordsFirstname.setText(searchInWordsFirstnameText);
                        ImageButton deleteFirstnameButton = new ImageButton(getContext());
                        deleteFirstnameButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
                        deleteFirstnameButton.setLayoutParams(new LinearLayout.LayoutParams(90, 90));
                        deleteFirstnameButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogFactory.setTitle("Delete Criterion")
                                        .setMessage("Are you sure?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                criteriaContainer.removeView(textFirstname);
                                                criteriaContainer.removeView(linearLayout);
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            }
                        });
                        criteriaContainer.addView(textFirstname);
                        linearLayout.addView(deleteFirstnameButton);
                        linearLayout.addView(firstname);
                        linearLayout.addView(searchInWordsFirstname);
                        criteriaContainer.addView(linearLayout);
                        break;
                    case "Lastname":
                        final TextView textLastname = new TextView(getContext());
                        textLastname.setText("Lastname: ");
                        final EditText lastname = new EditText(getContext());
                        lastname.setLayoutParams(new LinearLayout.LayoutParams(500,ViewGroup.LayoutParams.WRAP_CONTENT));
                        CheckBox searchInWordsLastname = new CheckBox(getContext());
                        CharSequence searchInWordsLastnameText = "Search in words";
                        searchInWordsLastname.setText(searchInWordsLastnameText);
                        ImageButton deleteLastnameButton = new ImageButton(getContext());
                        deleteLastnameButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
                        deleteLastnameButton.setLayoutParams(new LinearLayout.LayoutParams(90, 90));
                        deleteLastnameButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogFactory.setTitle("Delete Criterion")
                                        .setMessage("Are you sure?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                criteriaContainer.removeView(textLastname);
                                                criteriaContainer.removeView(linearLayout);
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            }
                        });
                        criteriaContainer.addView(textLastname);
                        linearLayout.addView(deleteLastnameButton);
                        linearLayout.addView(lastname);
                        linearLayout.addView(searchInWordsLastname);
                        criteriaContainer.addView(linearLayout);
                        break;
                    case "Birthdate":
                        final TextView textBirthdate = new TextView(getContext());
                        textBirthdate.setText("Birthdate: ");
                        EditText birthdate = new EditText(getContext());
                        birthdate.setLayoutParams(new LinearLayout.LayoutParams(500,ViewGroup.LayoutParams.WRAP_CONTENT));
                        ImageButton deleteBirthdateButton = new ImageButton(getContext());
                        deleteBirthdateButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
                        deleteBirthdateButton.setLayoutParams(new LinearLayout.LayoutParams(90, 90));
                        deleteBirthdateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogFactory.setTitle("Delete Criterion")
                                        .setMessage("Are you sure?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                criteriaContainer.removeView(textBirthdate);
                                                criteriaContainer.removeView(linearLayout);
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            }
                        });
                        criteriaContainer.addView(textBirthdate);
                        linearLayout.addView(deleteBirthdateButton);
                        linearLayout.addView(birthdate);
                        criteriaContainer.addView(linearLayout);
                        break;
                    case "State":
                        final TextView textState = new TextView(getContext());
                        textState.setText("State: ");
                        EditText state = new EditText(getContext());
                        state.setLayoutParams(new LinearLayout.LayoutParams(500,ViewGroup.LayoutParams.WRAP_CONTENT));
                        ImageButton deleteStateButton = new ImageButton(getContext());
                        deleteStateButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
                        deleteStateButton.setLayoutParams(new LinearLayout.LayoutParams(90, 90));
                        deleteStateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogFactory.setTitle("Delete Criterion")
                                        .setMessage("Are you sure?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                criteriaContainer.removeView(textState);
                                                criteriaContainer.removeView(linearLayout);
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            }
                        });
                        criteriaContainer.addView(textState);
                        linearLayout.addView(deleteStateButton);
                        linearLayout.addView(state);
                        criteriaContainer.addView(linearLayout);
                        break;
                    case "City":
                        final TextView textCity = new TextView(getContext());
                        textCity.setText("City: ");
                        EditText city = new EditText(getContext());
                        city.setLayoutParams(new LinearLayout.LayoutParams(500,ViewGroup.LayoutParams.WRAP_CONTENT));
                        ImageButton deleteCityButton = new ImageButton(getContext());
                        deleteCityButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
                        deleteCityButton.setLayoutParams(new LinearLayout.LayoutParams(90, 90));
                        deleteCityButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogFactory.setTitle("Delete Criterion")
                                        .setMessage("Are you sure?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                criteriaContainer.removeView(textCity);
                                                criteriaContainer.removeView(linearLayout);
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            }
                        });
                        criteriaContainer.addView(textCity);
                        linearLayout.addView(deleteCityButton);
                        linearLayout.addView(city);
                        criteriaContainer.addView(linearLayout);
                        break;
                    case "Address":
                        final TextView textAddress = new TextView(getContext());
                        textAddress.setText("Address: ");
                        EditText address = new EditText(getContext());
                        address.setLayoutParams(new LinearLayout.LayoutParams(500,ViewGroup.LayoutParams.WRAP_CONTENT));
                        ImageButton deleteAddressButton = new ImageButton(getContext());
                        deleteAddressButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
                        deleteAddressButton.setLayoutParams(new LinearLayout.LayoutParams(90, 90));
                        deleteAddressButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogFactory.setTitle("Delete Criterion")
                                        .setMessage("Are you sure?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                criteriaContainer.removeView(textAddress);
                                                criteriaContainer.removeView(linearLayout);
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            }
                        });
                        criteriaContainer.addView(textAddress);
                        linearLayout.addView(deleteAddressButton);
                        linearLayout.addView(address);
                        criteriaContainer.addView(linearLayout);
                        break;
                    case "Height":
                        final TextView textHeight = new TextView(getContext());
                        textHeight.setText("Height: ");
                        EditText height = new EditText(getContext());
                        height.setLayoutParams(new LinearLayout.LayoutParams(500,ViewGroup.LayoutParams.WRAP_CONTENT));
                        ImageButton deleteHeightButton = new ImageButton(getContext());
                        deleteHeightButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
                        deleteHeightButton.setLayoutParams(new LinearLayout.LayoutParams(90, 90));
                        deleteHeightButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogFactory.setTitle("Delete Criterion")
                                        .setMessage("Are you sure?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                criteriaContainer.removeView(textHeight);
                                                criteriaContainer.removeView(linearLayout);
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            }
                        });
                        criteriaContainer.addView(textHeight);
                        linearLayout.addView(deleteHeightButton);
                        linearLayout.addView(height);
                        criteriaContainer.addView(linearLayout);
                        break;
                    case "Weight":
                        final TextView textWeight = new TextView(getContext());
                        textWeight.setText("Weight: ");
                        EditText weight = new EditText(getContext());
                        weight.setLayoutParams(new LinearLayout.LayoutParams(500,ViewGroup.LayoutParams.WRAP_CONTENT));
                        ImageButton deleteWeightButton = new ImageButton(getContext());
                        deleteWeightButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
                        deleteWeightButton.setLayoutParams(new LinearLayout.LayoutParams(90, 90));
                        deleteWeightButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogFactory.setTitle("Delete Criterion")
                                        .setMessage("Are you sure?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                criteriaContainer.removeView(textWeight);
                                                criteriaContainer.removeView(linearLayout);
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            }
                        });
                        criteriaContainer.addView(textWeight);
                        linearLayout.addView(deleteWeightButton);
                        linearLayout.addView(weight);
                        criteriaContainer.addView(linearLayout);
                        break;
                    default:
                        break;
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String criteriaString = "";
                for (int j = 0; j < criteriaContainer.getChildCount(); j++ ) {
                    if (criteriaContainer.getChildAt(j) instanceof LinearLayout) {
                        LinearLayout linearLayout = (LinearLayout) criteriaContainer.getChildAt(j);
                        for (int i = 0; i < linearLayout.getChildCount(); i++) {
                            if (linearLayout.getChildAt(i) instanceof TextView) {
                                TextView t = (TextView) linearLayout.getChildAt(i);
                                if (t.getText().equals("Firstname: ")) {
                                    if (linearLayout.getChildAt(i + 2) instanceof EditText) {
                                        EditText e = (EditText) linearLayout.getChildAt(i + 2);
                                        if (linearLayout.getChildAt(i + 3) instanceof CheckBox) {
                                            CheckBox c = (CheckBox) linearLayout.getChildAt(i + 3);
                                            if (c.isChecked()) {
                                                criteriaString = criteriaString.concat("like-");
                                            }
                                        }
                                        criteriaString = criteriaString.concat("firstname=" + e.getText() + ";");
                                    }
                                }
                                if (t.getText().equals("Lastname: ")) {
                                    if (linearLayout.getChildAt(i + 2) instanceof EditText) {
                                        EditText e = (EditText) linearLayout.getChildAt(i + 2);
                                        if (linearLayout.getChildAt(i + 3) instanceof CheckBox) {
                                            CheckBox c = (CheckBox) linearLayout.getChildAt(i + 3);
                                            if (c.isChecked()) {
                                                criteriaString = criteriaString.concat("like-");
                                            }
                                        }
                                        criteriaString = criteriaString.concat("lastname=" + e.getText() + ";");
                                    }
                                }
                                if (t.getText().equals("Birthdate: ")) {
                                    if (linearLayout.getChildAt(i + 2) instanceof EditText) {
                                        EditText e = (EditText) linearLayout.getChildAt(i + 2);
                                        criteriaString = criteriaString.concat("birthdate=" + e.getText() + ";");
                                    }
                                }
                                if (t.getText().equals("State: ")) {
                                    if (linearLayout.getChildAt(i + 2) instanceof EditText) {
                                        EditText e = (EditText) linearLayout.getChildAt(i + 2);
                                        criteriaString = criteriaString.concat("state=" + e.getText() + ";");
                                    }
                                }
                                if (t.getText().equals("City: ")) {
                                    if (linearLayout.getChildAt(i + 2) instanceof EditText) {
                                        EditText e = (EditText) linearLayout.getChildAt(i + 2);
                                        criteriaString = criteriaString.concat("city=" + e.getText() + ";");
                                    }
                                }
                                if (t.getText().equals("Address: ")) {
                                    if (linearLayout.getChildAt(i + 2) instanceof EditText) {
                                        EditText e = (EditText) linearLayout.getChildAt(i + 2);
                                        criteriaString = criteriaString.concat("address=" + e.getText() + ";");
                                    }
                                }
                                if (t.getText().equals("Height: ")) {
                                    if (linearLayout.getChildAt(i + 2) instanceof EditText) {
                                        EditText e = (EditText) linearLayout.getChildAt(i + 2);
                                        criteriaString = criteriaString.concat("height=" + e.getText() + ";");
                                    }
                                }
                                if (t.getText().equals("Weight: ")) {
                                    if (linearLayout.getChildAt(i + 2) instanceof EditText) {
                                        EditText e = (EditText) linearLayout.getChildAt(i + 2);
                                        criteriaString = criteriaString.concat("weight=" + e.getText() + ";");
                                    }
                                }
                            }
                        }
                    }
                }
                final String criteria = new String(criteriaString);
                boolean subscribe = subscribeCheck.isChecked();
                GroupRequestDTO request = new GroupRequestDTO();
                request.setCriteria(criteria);
                request.setSubscription(subscribe);
                thirdPartyRequestView.onGroupRequest(request);
            }
        });
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ThirdPartyRequestView) {
            thirdPartyRequestView = (ThirdPartyRequestView) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ThirdPartyRequestView");
        }
    }
}
