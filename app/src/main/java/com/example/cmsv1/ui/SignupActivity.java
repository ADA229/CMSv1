package com.example.cmsv1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cmsv1.MainActivity;
import com.example.cmsv1.R;
import com.example.cmsv1.database.UserDao;

public class SignupActivity extends AppCompatActivity {
    private EditText nameEditText, phoneEditText, passwordEditText, confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        Button signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(v -> attemptSignup());
    }

    private void attemptSignup() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        registerUser(name, phone, password);
    }

    private void registerUser(String name, String phone, String password) {
        UserDao userDao = new UserDao(this);
        if (userDao.registerUser(name, phone, password)) {
            Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show();
        }
    }
}
