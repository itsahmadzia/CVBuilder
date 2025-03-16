package com.example.cvbuilder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Summary extends AppCompatActivity {

    private EditText etSummary;
    private Button btnSave, btnCancel;
    private String summaryText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        etSummary = findViewById(R.id.etSummary);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        summaryText = getIntent().getStringExtra("summary");
        if (summaryText != null) {
            etSummary.setText(summaryText);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSummary();
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

    private void saveSummary() {
        summaryText = etSummary.getText().toString();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("summary", summaryText);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
