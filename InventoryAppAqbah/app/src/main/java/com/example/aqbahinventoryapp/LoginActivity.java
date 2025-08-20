package com.example.aqbahinventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextInputEditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHelper(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        Button loginBtn = findViewById(R.id.btnLogin);
        Button signupBtn = findViewById(R.id.btnSignup);

        loginBtn.setOnClickListener(v -> doLogin(false));
        signupBtn.setOnClickListener(v -> doLogin(true)); // create account then login
    }

    private void doLogin(boolean createIfNew) {
        String user = etUsername.getText() == null ? "" : etUsername.getText().toString().trim();
        String pass = etPassword.getText() == null ? "" : etPassword.getText().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (createIfNew) {
            boolean created = db.createUser(user, pass);
            if (!created) {
                Toast.makeText(this, "User already exists or invalid input", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();
            }
        }

        boolean valid = db.validateUser(user, pass);
        if (valid) {
            startActivity(new Intent(this, InventoryActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
