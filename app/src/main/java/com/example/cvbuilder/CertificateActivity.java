package com.example.cvbuilder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CertificateActivity extends AppCompatActivity {

    private LinearLayout certificationContainer;
    private ArrayList<HashMap<String, String>> certificationList = new ArrayList<>();
    private Button btnAddCertification, btnSaveCertifications,btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        certificationContainer = findViewById(R.id.certificationContainer);
        btnAddCertification = findViewById(R.id.btnAddCertification);
        btnSaveCertifications = findViewById(R.id.btnSaveCertifications);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(v -> finish());

        btnAddCertification.setOnClickListener(v -> addCertificationField());
        btnSaveCertifications.setOnClickListener(v -> saveCertificationData());

        if (getIntent().hasExtra("certification_list")) {
            certificationList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("certification_list");
            loadCertificationFields();
        }

    }

    private void addCertificationField() {
        View certificationEntry = getLayoutInflater().inflate(R.layout.certification_entry, null);

        EditText etIssueDate = certificationEntry.findViewById(R.id.etIssueDate);
        EditText etExpirationDate = certificationEntry.findViewById(R.id.etExpirationDate);

        etIssueDate.setOnClickListener(v -> showDatePickerDialog(etIssueDate));
        etExpirationDate.setOnClickListener(v -> showDatePickerDialog(etExpirationDate));

        certificationContainer.addView(certificationEntry);
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editText.setText(date);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
    private void saveCertificationData() {
        certificationList.clear();
        for (int i = 0; i < certificationContainer.getChildCount(); i++) {
            View view = certificationContainer.getChildAt(i);
            EditText etCertificationName = view.findViewById(R.id.etCertificationName);
            EditText etIssuingOrganization = view.findViewById(R.id.etIssuingOrganization);
            EditText etIssueDate = view.findViewById(R.id.etIssueDate);
            EditText etExpirationDate = view.findViewById(R.id.etExpirationDate);

            HashMap<String, String> certificationData = new HashMap<>();
            certificationData.put("certification_name", etCertificationName.getText().toString());
            certificationData.put("issuing_organization", etIssuingOrganization.getText().toString());
            certificationData.put("issue_date", etIssueDate.getText().toString());
            certificationData.put("expiration_date", etExpirationDate.getText().toString());

            certificationList.add(certificationData);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("certification_list", certificationList);
        setResult(Activity.RESULT_OK, resultIntent);
        Toast.makeText(this, "Certifications Saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void loadCertificationFields() {
        certificationContainer.removeAllViews();
        for (HashMap<String, String> certificationData : certificationList) {
            View certificationEntry = getLayoutInflater().inflate(R.layout.certification_entry, null);

            EditText etCertificationName = certificationEntry.findViewById(R.id.etCertificationName);
            EditText etIssuingOrganization = certificationEntry.findViewById(R.id.etIssuingOrganization);
            EditText etIssueDate = certificationEntry.findViewById(R.id.etIssueDate);
            EditText etExpirationDate = certificationEntry.findViewById(R.id.etExpirationDate);

            etCertificationName.setText(certificationData.get("certification_name"));
            etIssuingOrganization.setText(certificationData.get("issuing_organization"));
            etIssueDate.setText(certificationData.get("issue_date"));
            etExpirationDate.setText(certificationData.get("expiration_date"));

            certificationContainer.addView(certificationEntry);
        }
    }
}
