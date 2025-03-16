package com.example.cvbuilder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ViewActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private TextView txtPersonalDetails, txtSummary, txtEducation, txtExperience, txtCertifications, txtReferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        imgProfile = findViewById(R.id.imgProfile);
        txtPersonalDetails = findViewById(R.id.txtPersonalDetails);
        txtSummary = findViewById(R.id.txtSummary);
        txtEducation = findViewById(R.id.txtEducation);
        txtExperience = findViewById(R.id.txtExperience);
        txtCertifications = findViewById(R.id.txtCertifications);
        txtReferences = findViewById(R.id.txtReferences);

        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve profile picture URI
            String profileUri = intent.getStringExtra("profile_picture_uri");
            if (profileUri != null) {
                imgProfile.setImageURI(Uri.parse(profileUri));
            }


            HashMap<String, String> personalDetails = (HashMap<String, String>) intent.getSerializableExtra("personal_details");
            if (personalDetails != null) {
                StringBuilder details = new StringBuilder();
                for (String key : personalDetails.keySet()) {
                    if(Objects.equals(personalDetails.get(key), "")){
                        continue;
                    }
                    details.append(personalDetails.get(key)).append("\n");
                }
                txtPersonalDetails.setText(details.toString());
            }

            // Retrieve and display summary
            String summary = intent.getStringExtra("summary");
            txtSummary.setText(summary != null ? summary : "No Summary");


            ArrayList<HashMap<String, String>> educationList = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("education_list");
            txtEducation.setText(formatListData(educationList));


            ArrayList<HashMap<String, String>> experienceList = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("experience_list");
            txtExperience.setText(formatListData(experienceList));


            ArrayList<HashMap<String, String>> certificationList = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("certification_list");
            txtCertifications.setText(formatListData(certificationList));


            ArrayList<HashMap<String, String>> referencesList = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("reference_list");
            txtReferences.setText(formatListData(referencesList));
        }
    }

    private String formatListData(ArrayList<HashMap<String, String>> list) {
        if (list == null || list.isEmpty()) {
            return "No Data Available";
        }

        StringBuilder formattedText = new StringBuilder();
        for (HashMap<String, String> item : list) {
            for (String key : item.keySet()) {
                if(Objects.equals(item.get(key), "")){
                    continue;
                }
                formattedText.append(key.toUpperCase().replace("_"," ")).append(": ").append(item.get(key)).append("\n");
            }
            formattedText.append("\n");
        }
        return formattedText.toString();
    }
}
