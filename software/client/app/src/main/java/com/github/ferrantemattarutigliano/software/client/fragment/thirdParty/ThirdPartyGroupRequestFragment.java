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
import android.widget.AdapterView;
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
        final LinearLayout criteriaContainer = v.findViewById(R.id.container_group_request_criteria);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.group_request_criteria, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        criteriaSpinner.setAdapter(adapter);
        criteriaSpinner.setId(0);

        criteriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                String item = criteriaSpinner.getSelectedItem().toString();
                if (item.equals("Firstname") || item.equals("Lastname")) {
                    nameCriterionLayout(item + ": ", linearLayout, criteriaContainer);
                }
                else {
                    genericCriterionLayout(item + ": ", linearLayout, criteriaContainer);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String criterion = "";
                String criteriaString = "";
                for (int j = 0; j < criteriaContainer.getChildCount(); j++) {
                    LinearLayout linearLayout;
                    if (criteriaContainer.getChildAt(j) instanceof TextView) {
                        TextView textView = (TextView) criteriaContainer.getChildAt(j);
                        criterion = textView.getText().toString();
                    }
                    if (criteriaContainer.getChildAt(j) instanceof LinearLayout) {
                        linearLayout = (LinearLayout) criteriaContainer.getChildAt(j);
                        for (int i = 0; i < linearLayout.getChildCount(); i++) {
                            if (linearLayout.getChildAt(i) instanceof TextView) {
                                criteriaString = criteriaString.concat(parseCriteria(i, criterion, linearLayout));
                            }
                        }
                    }
                }
                final String criteria = criteriaString;
                boolean subscribe = subscribeCheck.isChecked();
                GroupRequestDTO request = new GroupRequestDTO();
                request.setCriteria(criteria);
                request.setSubscription(subscribe);
                thirdPartyRequestView.onGroupRequest(request);
            }
        });
        return v;
    }

    private ImageButton createDeleteButton(final TextView text, final LinearLayout layout, final LinearLayout container) {
        ImageButton deleteButton = new ImageButton(getContext());
        deleteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(90, 90));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeView(text);
                container.removeView(layout);
            }
        });
        return deleteButton;
    }

    private void nameCriterionLayout(String criterion, LinearLayout layout, LinearLayout container) {
        final TextView textFirstname = new TextView(getContext());
        textFirstname.setText(criterion);

        final EditText firstname = new EditText(getContext());
        firstname.setLayoutParams(new LinearLayout.LayoutParams(500,ViewGroup.LayoutParams.WRAP_CONTENT));

        CheckBox searchInWordsFirstname = new CheckBox(getContext());
        searchInWordsFirstname.setText("Search in words");

        container.addView(textFirstname);
        layout.addView(createDeleteButton(textFirstname, layout, container));
        layout.addView(firstname);
        layout.addView(searchInWordsFirstname);
        container.addView(layout);
    }

    private void genericCriterionLayout(String criterion, LinearLayout layout, LinearLayout container) {
        final TextView textWeight = new TextView(getContext());
        textWeight.setText(criterion);
        EditText weight = new EditText(getContext());
        weight.setLayoutParams(new LinearLayout.LayoutParams(500,ViewGroup.LayoutParams.WRAP_CONTENT));

        container.addView(textWeight);
        layout.addView(createDeleteButton(textWeight, layout, container));
        layout.addView(weight);
        container.addView(layout);
    }

    private String parseNameCriterion(int i, LinearLayout linearLayout, String criteriaString, String criterion) {
        if (linearLayout.getChildAt(i) instanceof EditText) {
            EditText e = (EditText) linearLayout.getChildAt(i);
            if (linearLayout.getChildAt(i + 1) instanceof CheckBox) {
                CheckBox c = (CheckBox) linearLayout.getChildAt(i + 1);
                if (c.isChecked()) {
                    criteriaString = criteriaString.concat("like-");
                }
            }
            return criteriaString.concat(criterion + e.getText() + ";");
        }
        return "";
    }

    private String parseGenericCriterion(int i, LinearLayout linearLayout, String criteriaString, String criterion) {
        if (linearLayout.getChildAt(i) instanceof EditText) {
            EditText e = (EditText) linearLayout.getChildAt(i);
            return criteriaString.concat(criterion + e.getText() + ";");
        }
        return "";
    }

    private String parseCriteria(int i, String criterion, LinearLayout linearLayout) {
        String criteriaString = "";
        if (criterion.equals("Firstname: ")) {
            criteriaString = parseNameCriterion(i, linearLayout, criteriaString, "firstname=");
        }
        if (criterion.equals("Lastname: ")) {
            criteriaString = parseNameCriterion(i, linearLayout, criteriaString, "lastname=");
        }
        if (criterion.equals("Birthdate: ")) {
            criteriaString = parseGenericCriterion(i, linearLayout, criteriaString, "birthdate=");
        }
        if (criterion.equals("State: ")) {
            criteriaString = parseGenericCriterion(i, linearLayout, criteriaString, "state=");
        }
        if (criterion.equals("City: ")) {
            criteriaString = parseGenericCriterion(i, linearLayout, criteriaString, "city=");
        }
        if (criterion.equals("Address: ")) {
            criteriaString = parseGenericCriterion(i, linearLayout, criteriaString, "address=");
        }
        if (criterion.equals("Height: ")) {
            criteriaString = parseGenericCriterion(i, linearLayout, criteriaString, "height=");
        }
        if (criterion.equals("Weight: ")) {
            criteriaString = parseGenericCriterion(i, linearLayout, criteriaString, "weight=");
        }
        return criteriaString;
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