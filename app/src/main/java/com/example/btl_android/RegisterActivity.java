package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.btl_android.dal.UserSQLiteHelper;
import com.example.btl_android.model.User;
import com.example.btl_android.util.CommonUtil;
import com.example.btl_android.util.HashUtil;
import com.example.btl_android.util.PasswordUtil;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE = 9999;
    private EditText name, username, password, repass, email, phone;
    private Button btnRegister, btCancel;
    private UserSQLiteHelper userSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        btCancel.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Register");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initView() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repass = findViewById(R.id.repass);
        btnRegister = findViewById(R.id.btnRegister);
        btCancel = findViewById(R.id.btnCancel);
        userSQLiteHelper = new UserSQLiteHelper(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnRegister) {
            String nameInput = name.getText().toString().trim();
            String emailInput = email.getText().toString().trim();
            String phoneInput = phone.getText().toString().trim();
            String usernameInput = username.getText().toString().trim();
            String passwordInput = password.getText().toString();
            String repassInput = repass.getText().toString();

            if (nameInput.isEmpty() || usernameInput.isEmpty() || passwordInput.isEmpty() || repassInput.isEmpty() || emailInput.isEmpty() || phoneInput.isEmpty()) {
                Toast.makeText(getApplicationContext(), "All fields are required!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!CommonUtil.isValidEmail(emailInput)) {
                Toast.makeText(getApplicationContext(), "Invalid email format!", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            if (!CommonUtil.isValidPhoneNumber(phoneInput)) {
                Toast.makeText(getApplicationContext(), "Invalid phone number!", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            if (CommonUtil.isValidUsername(usernameInput)) {
                Toast.makeText(getApplicationContext(), "Invalid username format!", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            if (!passwordInput.equals(repassInput)) {
                Toast.makeText(getApplicationContext(), "Passwords do not match!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!PasswordUtil.isStrongPassword(passwordInput)) {
                Toast.makeText(getApplicationContext(), "Password must be strong!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the username already exists
            if (userSQLiteHelper.isExisted(usernameInput)) {
                Toast.makeText(getApplicationContext(), "Username already exists!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (userSQLiteHelper.isEmailExists(emailInput)) {
                Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            // Generate salt and hash the password with salt
            String salt = HashUtil.generateSalt();
            String hashedPassword = null;
            try {
                hashedPassword = HashUtil.hashPasswordWithSalt(passwordInput, salt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Create user and store in the database
            User user = new User(nameInput, usernameInput, hashedPassword, "customer", emailInput, phoneInput, salt);
            userSQLiteHelper.addUser(user);

            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = new Intent();
            intent.putExtra("user", user);
            setResult(RESULT_OK, intent);
            finish();
        }

        if (view == btCancel) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}