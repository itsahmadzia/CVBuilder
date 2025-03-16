package com.example.cvbuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.HashMap;

public class DataEntryActivity extends AppCompatActivity {

    private LinearLayout formContainer;
    private String screenType;
    private HashMap<String, EditText> inputFields = new HashMap<>();
    private ImageView profileImageView;
    private Uri selectedImageUri = null;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        profileImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        screenType = getIntent().getStringExtra("SCREEN_TYPE");
        String previousData = getIntent().getStringExtra("PREVIOUS_DATA");

        TextView title = findViewById(R.id.screenTitle);
        title.setText(screenType);

        formContainer = findViewById(R.id.formContainer);

        generateFormFields(screenType, previousData);

        // Use the buttons from layout instead of creating them dynamically
        Button saveButton = findViewById(R.id.btnSave);
        Button cancelButton = findViewById(R.id.btnCancel);

        saveButton.setOnClickListener(v -> saveData());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void generateFormFields(String screenType, String previousData) {
        // Clear any existing views first
        formContainer.removeAllViews();
        inputFields.clear();

        switch (screenType) {
            case "Profile Picture":
                addProfilePictureField();
                break;
            case "Personal Details":
                addTextField("Full Name", getValueForField(previousData,"Full Name"));
                addTextField("Email", getValueForField(previousData, "Email"));
                addTextField("Phone", getValueForField(previousData, "Phone"));
                break;
            case "Summary":
                addTextField("Summary (Brief about yourself)", getValueForField(previousData,"Summary (Brief about yourself)"));
                break;
            case "Education":
                addTextField("Degree", getValueForField(previousData, "Degree"));
                addTextField("Institution", getValueForField(previousData, "Institution"));
                addTextField("Year of Graduation", getValueForField(previousData, "Year of Graduation"));
                break;
            case "Experience":
                addTextField("Company", getValueForField(previousData, "Company"));
                addTextField("Job Title", getValueForField(previousData, "Job Title"));
                addTextField("Years of Experience", getValueForField(previousData, "Years of Experience"));
                break;
            case "Certifications":
                addTextField("Certification Name", getValueForField(previousData, "Certification Name"));
                addTextField("Issuing Organization", getValueForField(previousData, "Issuing Organization"));
                break;
            case "References":
                addTextField("Reference Name", getValueForField(previousData, "Reference Name"));
                addTextField("Contact Info", getValueForField(previousData, "Contact Info"));
                break;
        }
    }

    // Helper method to extract field values from the saved data string
    private String getValueForField(String previousData, String fieldName) {
        if (previousData == null || previousData.isEmpty()) {
            return null;
        }


        String[] lines = previousData.split("\n");
        for (String line : lines) {
            if (line.startsWith(fieldName + ": ")) {
                return line.substring((fieldName + ": ").length());
            }
        }
        return null;
    }

    private void addTextField(String hint, String previousData) {
        EditText editText = new EditText(this);
        editText.setHint(hint);
        if (previousData != null) {
            editText.setText(previousData);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 30);

        editText.setLayoutParams(params);
        formContainer.addView(editText);
        inputFields.put(hint, editText);
    }

    private void addProfilePictureField() {
        profileImageView = new ImageView(this);
        profileImageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                400
        ));
        profileImageView.setAdjustViewBounds(true);
        profileImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Button selectImageButton = new Button(this);
        selectImageButton.setText("Select Profile Picture");
        selectImageButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        selectImageButton.setOnClickListener(v -> openImagePicker());

        formContainer.addView(profileImageView);
        formContainer.addView(selectImageButton);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void saveData() {
        // Create structured data to preserve field names and values
        StringBuilder collectedData = new StringBuilder();
        for (String key : inputFields.keySet()) {
            collectedData.append(key).append(": ").append(inputFields.get(key).getText().toString()).append("\n");
        }

        // For profile image, store the URI
        if (screenType.equals("Profile Picture") && selectedImageUri != null) {
            collectedData.append("ImageURI: ").append(selectedImageUri.toString());
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("SCREEN_TYPE", screenType);
        resultIntent.putExtra("ENTERED_DATA", collectedData.toString().trim());

        if (screenType.equals("Profile Picture") && selectedImageUri != null) {
            resultIntent.putExtra("IMAGE_URI", selectedImageUri.toString());
        }

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}