package com.example.cvbuilder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private HashMap<String, String> personalDetails = new HashMap<>();
    private String summary = "";
    private ArrayList<HashMap<String, String>> educationList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> experienceList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> certificationList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> referencesList = new ArrayList<>();
    Uri selectedImageUri;

    private ActivityResultLauncher<Intent> personalDetailsLauncher;
    private ActivityResultLauncher<Intent> summaryLauncher;
    private ActivityResultLauncher<Intent> educationLauncher;
    private ActivityResultLauncher<Intent> experienceLauncher;
    private ActivityResultLauncher<Intent> certificationLauncher;
    private ActivityResultLauncher<Intent> referencesLauncher;
    private ActivityResultLauncher<Intent> profilePictureLauncher; // New

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnProfilePicture = findViewById(R.id.btnProfilePicture);
        Button btnPersonalDetails = findViewById(R.id.btnPersonalDetails);
        Button btnSummary = findViewById(R.id.btnSummary);
        Button btnEducation = findViewById(R.id.btnEducation);
        Button btnExperience = findViewById(R.id.btnExperience);
        Button btnCertifications = findViewById(R.id.btnCertifications);
        Button btnReferences = findViewById(R.id.btnReferences);
        Button btnViewProfile = findViewById(R.id.btnViewProfile);

        // Profile Picture Launcher (Gallery)
        profilePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleProfilePictureResult
        );

        btnProfilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            profilePictureLauncher.launch(intent);
        });
        btnViewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewActivity.class);
            intent.putExtra("profile_picture_uri", selectedImageUri != null ? selectedImageUri.toString() : null);
            intent.putExtra("personal_details", personalDetails);
            intent.putExtra("summary", summary);
            intent.putExtra("education_list", educationList);
            intent.putExtra("experience_list", experienceList);
            intent.putExtra("certification_list", certificationList);
            intent.putExtra("reference_list", referencesList);
            startActivity(intent);
        });
        // Certification Launcher
        certificationLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleCertificationResult
        );

        btnCertifications.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CertificateActivity.class);
            intent.putExtra("certification_list", certificationList);
            certificationLauncher.launch(intent);
        });

        // References Launcher
        referencesLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleReferencesResult
        );

        btnReferences.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReferencesActivity.class);
            intent.putExtra("reference_list", referencesList);
            referencesLauncher.launch(intent);
        });

        personalDetailsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handlePersonalDetailsResult
        );

        summaryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleSummaryResult
        );

        educationLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleEducationResult
        );

        experienceLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleExperienceResult
        );

        btnPersonalDetails.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PersonalDetailsActivity.class);
            intent.putExtra("personal_details", personalDetails);
            personalDetailsLauncher.launch(intent);
        });

        btnSummary.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Summary.class);
            intent.putExtra("summary", summary);
            summaryLauncher.launch(intent);
        });

        btnEducation.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EducationActivity.class);
            intent.putExtra("education_list", educationList);
            educationLauncher.launch(intent);
        });

        btnExperience.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExperienceActivity.class);
            intent.putExtra("experience_list", experienceList);
            experienceLauncher.launch(intent);
        });
    }

    private void handleProfilePictureResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
           selectedImageUri = result.getData().getData();
            if (selectedImageUri != null) {
                Toast.makeText(this, "Profile Picture Selected: " + selectedImageUri.toString(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void handlePersonalDetailsResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            personalDetails = (HashMap<String, String>) result.getData().getSerializableExtra("personal_details");
            Toast.makeText(this, "Personal Details Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSummaryResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            summary = result.getData().getStringExtra("summary");
            Toast.makeText(this, "Summary Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleEducationResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            educationList = (ArrayList<HashMap<String, String>>) result.getData().getSerializableExtra("education_list");
            Toast.makeText(this, "Education Details Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleExperienceResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            experienceList = (ArrayList<HashMap<String, String>>) result.getData().getSerializableExtra("experience_list");
            Toast.makeText(this, "Experience Details Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCertificationResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            certificationList = (ArrayList<HashMap<String, String>>) result.getData().getSerializableExtra("certification_list");
            Toast.makeText(this, "Certifications Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleReferencesResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            referencesList = (ArrayList<HashMap<String, String>>) result.getData().getSerializableExtra("reference_list");
            Toast.makeText(this, "References Updated", Toast.LENGTH_SHORT).show();
        }
    }
}
