package com.example.cmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cmsv1.R;
import com.example.cmsv1.database.UserDao;
import android.util.Pair; // Add this import
import java.util.ArrayList; // Add this import
import java.util.List;

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
        String creditDetails = userDao.TotalCreditAmount(userId);
        
        // Update this block to handle the new return type
        List<Pair<Integer, String>> transactionHistory = userDao.getAllTransactionHistory(userId);
        List<String> transactionDetails = new ArrayList<>();
        for (Pair<Integer, String> transaction : transactionHistory) {
            transactionDetails.add(transaction.second);
        }

        // Set up the ListView to display the transaction history
        ListView transactionListView = findViewById(R.id.transactionListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionDetails);
        transactionListView.setAdapter(adapter);

        String amount = creditDetails != null ? creditDetails : "N/A";
        String description =  " ";

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        welcomeTextView.setText("Welcome, " + username + " !");

        TextView amountTextView = findViewById(R.id.amountTextView);
        amountTextView.setText("Amount to be paid: " + amount);

        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(description);
    }
}