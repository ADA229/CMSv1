package com.example.cmsv1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cmsv1.ui.HomeActivity;
import com.example.cmsv1.ui.LoginActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Check if user is logged in
        if (!isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        
        if (isLoggedIn()) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("userId", getUserId());
            startActivity(intent);
            finish();
            return;
        }
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private boolean isLoggedIn() {
        // TODO: Implement proper session management
        // For now, return a dummy value
        return false;
    }
    
    private int getUserId() {
        // TODO: Implement proper session management to retrieve the logged-in user's ID
        // For now, return a dummy user ID
        return 1;
    }
}