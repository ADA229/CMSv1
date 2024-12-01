package com.example.cmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

// Ensure the import statement for the R class is present
import com.example.cmsv1.R;
import com.example.cmsv1.database.UserDao;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Ensure you have a layout file named activity_home

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1);

        if (userId == -1) {
            // Handle the case where userId is not passed correctly
            // For example, redirect to login or show an error message
            // finish(); // Uncomment to close the activity
            return;
        }

        UserDao userDao = new UserDao(this);
        String username = userDao.getUserName(userId);
        // Fetch credit details for the correct user
        String[] creditDetails = userDao.getCreditDetails(userId);
        String amount = creditDetails[0] != null ? creditDetails[0] : "N/A";
        String description = creditDetails[1] != null ? creditDetails[1] : "N/A";

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome, " + username + "!");

        TextView amountTextView = findViewById(R.id.amountTextView);
        amountTextView.setText("Amount to be paid: " + amount);

        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(description);
    }
}