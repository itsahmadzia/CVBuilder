package com.example.cvbuilder;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class PersonalDetailsActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPhone;
    private Button btnSave, btnCancel;
    private HashMap<String, String> personalDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        personalDetails = (HashMap<String, String>) getIntent().getSerializableExtra("personal_details");
        if (personalDetails == null) {
            personalDetails = new HashMap<>();
        }
        loadSavedData();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    private void loadSavedData() {
        etFirstName.setText(personalDetails.getOrDefault("first_name", ""));
        etLastName.setText(personalDetails.getOrDefault("last_name", ""));
        etEmail.setText(personalDetails.getOrDefault("email", ""));
        etPhone.setText(personalDetails.getOrDefault("phone", ""));
    }

    private void saveData() {
        personalDetails.put("first_name", etFirstName.getText().toString());
        personalDetails.put("last_name", etLastName.getText().toString());
        personalDetails.put("email", etEmail.getText().toString());
        personalDetails.put("phone", etPhone.getText().toString());

        Intent resultIntent = new Intent();
        resultIntent.putExtra("personal_details", personalDetails);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
