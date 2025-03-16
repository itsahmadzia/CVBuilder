package com.example.cvbuilder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ExperienceActivity extends AppCompatActivity {

    private LinearLayout experienceContainer;
    private ArrayList<HashMap<String, String>> experienceList = new ArrayList<>();
    private Button btnAddExperience, btnSaveExperience,btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);

        experienceContainer = findViewById(R.id.experienceContainer);
        btnAddExperience = findViewById(R.id.btnAddExperience);
        btnSaveExperience = findViewById(R.id.btnSaveExperience);
        btnCancel = findViewById(R.id.btnCancel);

        btnAddExperience.setOnClickListener(v -> addExperienceField());

        btnSaveExperience.setOnClickListener(v -> saveExperienceData());
        btnCancel.setOnClickListener(v -> finish());

        if (getIntent().hasExtra("experience_list")) {
            experienceList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("experience_list");
            loadExperienceFields();
        }
    }

    private void addExperienceField() {
        View experienceEntry = getLayoutInflater().inflate(R.layout.experience_entry, null);
        EditText etFromDate = experienceEntry.findViewById(R.id.etFromDate);
        EditText etToDate = experienceEntry.findViewById(R.id.etToDate);
        CheckBox cbCurrentlyWorking = experienceEntry.findViewById(R.id.cbCurrentlyWorking);

        etFromDate.setOnClickListener(v -> showDatePickerDialog(etFromDate));
        etToDate.setOnClickListener(v -> showDatePickerDialog(etToDate));

        cbCurrentlyWorking.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etToDate.setText("Present");
                etToDate.setEnabled(false);
            } else {
                etToDate.setText("");
                etToDate.setEnabled(true);
            }
        });



        experienceContainer.addView(experienceEntry);
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

    private void saveExperienceData() {
        experienceList.clear();
        for (int i = 0; i < experienceContainer.getChildCount(); i++) {
            View view = experienceContainer.getChildAt(i);
            EditText etCompany = view.findViewById(R.id.etCompany);
            EditText etFromDate = view.findViewById(R.id.etFromDate);
            EditText etToDate = view.findViewById(R.id.etToDate);
            EditText etRole = view.findViewById(R.id.etRole);
            EditText etResponsibilities = view.findViewById(R.id.etResponsibilities);
            CheckBox cbCurrentlyWorking = view.findViewById(R.id.cbCurrentlyWorking);

            HashMap<String, String> experienceData = new HashMap<>();
            experienceData.put("company", etCompany.getText().toString());
            experienceData.put("from_date", etFromDate.getText().toString());
            experienceData.put("to_date", cbCurrentlyWorking.isChecked() ? "Present" : etToDate.getText().toString());
            experienceData.put("role", etRole.getText().toString());
            experienceData.put("responsibilities", etResponsibilities.getText().toString());

            experienceList.add(experienceData);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("experience_list", experienceList);
        setResult(Activity.RESULT_OK, resultIntent);
        Toast.makeText(this, "Experience Details Saved", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void loadExperienceFields() {
        experienceContainer.removeAllViews(); // Clear existing views before loading

        for (HashMap<String, String> experienceData : experienceList) {
            View experienceEntry = getLayoutInflater().inflate(R.layout.experience_entry, null);

            EditText etCompany = experienceEntry.findViewById(R.id.etCompany);
            EditText etFromDate = experienceEntry.findViewById(R.id.etFromDate);
            EditText etToDate = experienceEntry.findViewById(R.id.etToDate);
            EditText etRole = experienceEntry.findViewById(R.id.etRole);
            EditText etResponsibilities = experienceEntry.findViewById(R.id.etResponsibilities);
            CheckBox cbCurrentlyWorking = experienceEntry.findViewById(R.id.cbCurrentlyWorking);

            etCompany.setText(experienceData.get("company"));
            etFromDate.setText(experienceData.get("from_date"));
            etToDate.setText(experienceData.get("to_date"));
            etRole.setText(experienceData.get("role"));
            etResponsibilities.setText(experienceData.get("responsibilities"));

            // Handle "Present" case for currently working checkbox
            if ("Present".equals(experienceData.get("to_date"))) {
                cbCurrentlyWorking.setChecked(true);
                etToDate.setEnabled(false);
            } else {
                cbCurrentlyWorking.setChecked(false);
                etToDate.setEnabled(true);
            }

            // Set date pickers again for editing
            etFromDate.setOnClickListener(v -> showDatePickerDialog(etFromDate));
            etToDate.setOnClickListener(v -> showDatePickerDialog(etToDate));

            cbCurrentlyWorking.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    etToDate.setText("Present");
                    etToDate.setEnabled(false);
                } else {
                    etToDate.setText("");
                    etToDate.setEnabled(true);
                }
            });

            experienceContainer.addView(experienceEntry);
        }
    }

}
