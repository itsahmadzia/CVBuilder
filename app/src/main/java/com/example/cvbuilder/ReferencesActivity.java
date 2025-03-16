package com.example.cvbuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ReferencesActivity extends AppCompatActivity {

    private LinearLayout referenceContainer;
    private ArrayList<HashMap<String, String>> referenceList = new ArrayList<>();
    private Button btnAddReference, btnSaveReferences, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_references);

        referenceContainer = findViewById(R.id.referenceContainer);
        btnAddReference = findViewById(R.id.btnAddReference);
        btnSaveReferences = findViewById(R.id.btnSaveReferences);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(v -> finish());
        btnAddReference.setOnClickListener(v -> addReferenceField());
        btnSaveReferences.setOnClickListener(v -> saveReferenceData());

        if (getIntent().hasExtra("reference_list")) {
            referenceList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("reference_list");
            loadReferenceFields();
        }
    }

    private void addReferenceField() {
        View referenceEntry = getLayoutInflater().inflate(R.layout.reference_entry, null);

        referenceContainer.addView(referenceEntry);
    }

    private void saveReferenceData() {
        referenceList.clear();
        for (int i = 0; i < referenceContainer.getChildCount(); i++) {
            View view = referenceContainer.getChildAt(i);
            EditText etRefName = view.findViewById(R.id.etRefName);
            EditText etRefDesignation = view.findViewById(R.id.etRefDesignation);
            EditText etRefCompany = view.findViewById(R.id.etRefCompany);
            EditText etRefContact = view.findViewById(R.id.etRefContact);

            HashMap<String, String> referenceData = new HashMap<>();
            referenceData.put("name", etRefName.getText().toString());
            referenceData.put("designation", etRefDesignation.getText().toString());
            referenceData.put("company", etRefCompany.getText().toString());
            referenceData.put("contact", etRefContact.getText().toString());

            referenceList.add(referenceData);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("reference_list", referenceList);
        setResult(Activity.RESULT_OK, resultIntent);
        Toast.makeText(this, "References Saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void loadReferenceFields() {
        referenceContainer.removeAllViews();
        for (HashMap<String, String> referenceData : referenceList) {
            View referenceEntry = getLayoutInflater().inflate(R.layout.reference_entry, null);

            EditText etRefName = referenceEntry.findViewById(R.id.etRefName);
            EditText etRefDesignation = referenceEntry.findViewById(R.id.etRefDesignation);
            EditText etRefCompany = referenceEntry.findViewById(R.id.etRefCompany);
            EditText etRefContact = referenceEntry.findViewById(R.id.etRefContact);

            etRefName.setText(referenceData.get("name"));
            etRefDesignation.setText(referenceData.get("designation"));
            etRefCompany.setText(referenceData.get("company"));
            etRefContact.setText(referenceData.get("contact"));

            referenceContainer.addView(referenceEntry);
        }
    }
}
