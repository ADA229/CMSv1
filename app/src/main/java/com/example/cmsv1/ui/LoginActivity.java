package com.example.cmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmsv1.MainActivity;
import com.example.cmsv1.R;
import com.example.cmsv1.database.UserDao;
import com.example.cmsv1.ui.ShopKeeperActivity; // Ensure this class exists and is correctly named

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;
    private static int[] globalUserId = new int[1]; // Global array to store user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        loginButton.setOnClickListener(v -> attemptLogin());
        signupButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }

    private void attemptLogin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        authenticateUser(username, password);
    }

    private void authenticateUser(String username, String password) {
        UserDao userDao = new UserDao(this);
        Pair<Boolean, Integer> authResult = userDao.authenticateUser(username, password);
        if (authResult.first) {
            globalUserId[0] = authResult.second; // Store user ID in global array
            Intent intent;
            if (authResult.second == 777) {
                intent = new Intent(this, ShopKeeperActivity.class);
            } else {
                intent = new Intent(this, HomeActivity.class);
            }
            intent.putExtra("userId", authResult.second);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
