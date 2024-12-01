package com.example.cmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

// Ensure the import statement for the R class is present
import com.example.cmsv1.R;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Ensure you have a layout file named activity_home

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome, " + username + "!");
    }
}