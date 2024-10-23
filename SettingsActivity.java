package com.example.inventorymanagementapp;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingsapp);

        RadioGroup radioGroupTextSize = findViewById(R.id.radioGroupTextSize);

        // Get the saved text size preference
        String textSizePreference = SettingsApp.getTextSizePreference(this);

        // Set the checked state of radio buttons based on the saved preference
        if ("small".equals(textSizePreference)) {
            radioGroupTextSize.check(R.id.radioSmallText);
        } else if ("large".equals(textSizePreference)) {
            radioGroupTextSize.check(R.id.radioLargeText);
        } else if ("extra_large".equals(textSizePreference)) {
            radioGroupTextSize.check(R.id.radioExtraLargeText);
        } else {
            radioGroupTextSize.check(R.id.radioMediumText);
        }

        // Listen for changes in text size preference
        radioGroupTextSize.setOnCheckedChangeListener((group, checkedId) -> {
            String textSize;
            if (checkedId == R.id.radioSmallText) {
                textSize = "small";
            } else if (checkedId == R.id.radioLargeText) {
                textSize = "large";
            } else if (checkedId == R.id.radioExtraLargeText) {
                textSize = "extra_large";
            } else {
                textSize = "medium";
            }
            // Set the text size preference
            SettingsApp.setTextSizePreference(SettingsActivity.this, textSize);
        });
    }
}
