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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class EducationActivity extends AppCompatActivity {

    private LinearLayout educationContainer;
    private ArrayList<HashMap<String, String>> educationList = new ArrayList<>();
    private Button btnAddEducation, btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        educationContainer = findViewById(R.id.educationContainer);
        btnAddEducation = findViewById(R.id.btnAddEducation);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        btnAddEducation.setOnClickListener(v -> addEducationField());

        btnSave.setOnClickListener(v -> saveEducationData());

        btnCancel.setOnClickListener(v -> finish());

        if (getIntent().hasExtra("education_list")) {
            educationList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("education_list");
            loadEducationFields();
        }
    }

    private void addEducationField() {
        View educationEntry = getLayoutInflater().inflate(R.layout.education_entry, null);
        EditText etPassingDate = educationEntry.findViewById(R.id.etPassingDate);

        etPassingDate.setOnClickListener(v -> showDatePickerDialog(etPassingDate));

        educationContainer.addView(educationEntry);
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

    private void saveEducationData() {
        educationList.clear();
        for (int i = 0; i < educationContainer.getChildCount(); i++) {
            View view = educationContainer.getChildAt(i);
            EditText etInstitute = view.findViewById(R.id.etInstitute);
            EditText etPassingDate = view.findViewById(R.id.etPassingDate);
            EditText etAddress = view.findViewById(R.id.etAddress);

            HashMap<String, String> educationData = new HashMap<>();
            educationData.put("institute", etInstitute.getText().toString());
            educationData.put("passing_date", etPassingDate.getText().toString());
            educationData.put("address", etAddress.getText().toString());

            educationList.add(educationData);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("education_list", educationList);
        setResult(Activity.RESULT_OK, resultIntent);
        Toast.makeText(this, "Education Details Saved", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void loadEducationFields() {
        educationContainer.removeAllViews();
        for (HashMap<String, String> educationData : educationList) {
            View educationEntry = getLayoutInflater().inflate(R.layout.education_entry, educationContainer, false);

            EditText etInstitute = educationEntry.findViewById(R.id.etInstitute);
            EditText etPassingDate = educationEntry.findViewById(R.id.etPassingDate);
            EditText etAddress = educationEntry.findViewById(R.id.etAddress);

            etInstitute.setText(educationData.get("institute"));
            etPassingDate.setText(educationData.get("passing_date"));
            etAddress.setText(educationData.get("address"));

            educationContainer.addView(educationEntry);
        }
    }
}

