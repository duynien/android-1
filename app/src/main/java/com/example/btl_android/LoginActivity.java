package com.example.btl_android;

import static com.example.btl_android.util.CommonUtil.isValidEmail;
import static com.example.btl_android.util.CommonUtil.isValidUsername;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.btl_android.dal.UserSQLiteHelper;
import com.example.btl_android.model.User;
import com.example.btl_android.util.HashUtil;
import com.example.btl_android.util.CommonUtil;
import com.example.btl_android.util.JavaMailUtil;
import com.example.btl_android.util.RateLimiter;
import com.example.btl_android.util.PasswordUtil;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MAX_REQUESTS_PER_MINUTE = 5;
    private static final long TIME_WINDOW_MILLIS = TimeUnit.MINUTES.toMillis(1);
    private RateLimiter rateLimiter = new RateLimiter(MAX_REQUESTS_PER_MINUTE, TIME_WINDOW_MILLIS);
    private long tokenGenerationTime;
    private static final long TOKEN_EXPIRATION_TIME = 5 * 60 * 1000;
    private EditText etUsername, etPassword;
    private TextView forgotPassword;
    private Button btnLogin, btnRegister;
    private UserSQLiteHelper userSQLiteHelper;
    private String verificationCode;
    private User user;
    private int attemptCount = 0; // Track the number of verification attempts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        forgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Login");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    private void initView() {
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        forgotPassword = findViewById(R.id.forgotPassword);
        userSQLiteHelper = new UserSQLiteHelper(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogin) {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                if (!CommonUtil.isValidUsername(username)) {
                    Toast.makeText(this, "Invalid username. Please enter a valid username.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rateLimiter.isAllowed(username)) {
                    User account = userSQLiteHelper.getAccount(username, password);
                    if (account == null) {
                        Toast.makeText(this, "Wrong username or password!", Toast.LENGTH_SHORT).show();
                    } else {
                        user = account;
                        String userEmail = account.getEmail();
                        sendVerificationCode(userEmail);
                        showVerificationDialog();
                    }
                }
                else {
                    Toast.makeText(this, "Too many login attempts. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter all fields.", Toast.LENGTH_SHORT).show();
            }
        }

        if (view == btnRegister) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, 9999);
        }
    }

    @SuppressLint("DefaultLocale")
    private void sendVerificationCode(String email) {
        Random random = new Random();
        verificationCode = String.format("%06d", random.nextInt(1000000));
        tokenGenerationTime = System.currentTimeMillis();
        JavaMailUtil.sendVerificationEmail(email, verificationCode);
        Toast.makeText(getApplicationContext(), "Verification code sent to your email!", Toast.LENGTH_SHORT).show();
    }

    private void showVerificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_verification, null);
        builder.setView(dialogView);
        // Get references to the views
        EditText input = dialogView.findViewById(R.id.verification_code_input);
        Button btnVerify = dialogView.findViewById(R.id.btn_verify);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        AlertDialog dialog = builder.create();
        // Handle Verify button click
        btnVerify.setOnClickListener(v -> {
            String enteredCode = input.getText().toString().trim();
            long currentTime = System.currentTimeMillis();
            if (currentTime - tokenGenerationTime > TOKEN_EXPIRATION_TIME) {
                Toast.makeText(this, "Verification code has expired. Please request a new code.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else if (enteredCode.equals(verificationCode)) {
                attemptCount = 0;
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("account", user);
                startActivity(intent);
                dialog.dismiss();
            } else {
                attemptCount++;
                if (attemptCount >= 3) {
                    Toast.makeText(this, "Too many failed attempts. Returning to login.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Incorrect code. Attempt " + attemptCount + "/3", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle Cancel button click
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
        builder.setView(dialogView);

        EditText usernameInput = dialogView.findViewById(R.id.usernameInput);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);
        Button btnVerifyCode = dialogView.findViewById(R.id.btnVerifyCode);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel); // Cancel button
        EditText verificationInput = dialogView.findViewById(R.id.verification_code_input); // The EditText for code
        AlertDialog dialog = builder.create();

        // Submit button to send verification code
        btnSubmit.setOnClickListener(view -> {
            String username = usernameInput.getText().toString().trim();
            if (isValidUsername(username)) {
                User user = userSQLiteHelper.getUserByUsername(username);
                if (user != null) {
                    sendVerificationCode(user.getEmail());
                    usernameInput.setVisibility(View.GONE); // Ẩn ô nhập username
                    btnSubmit.setVisibility(View.GONE); // Ẩn nút Submit
                    btnVerifyCode.setVisibility(View.VISIBLE); // Hiển thị nút Verify Code
                    verificationInput.setVisibility(View.VISIBLE); // Hiển thị ô nhập mã xác minh
                    btnCancel.setVisibility(View.VISIBLE); // Hiển thị nút Cancel
                    Toast.makeText(this, "Verification code sent to email!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Email not registered!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter a valid email!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(view -> dialog.dismiss()); // Cancel button

        // Verify code button
        btnVerifyCode.setOnClickListener(view -> {
            String enteredCode = verificationInput.getText().toString().trim();
            long currentTime = System.currentTimeMillis();
            if (currentTime - tokenGenerationTime > TOKEN_EXPIRATION_TIME) {
                Toast.makeText(this, "Verification code expired. Request a new one.", Toast.LENGTH_SHORT).show();
            } else if (enteredCode.equals(verificationCode)) {
                Toast.makeText(this, "Verification successful! You can now reset your password.", Toast.LENGTH_SHORT).show();
                verificationInput.setVisibility(View.GONE); // Ẩn ô nhập mã xác minh
                btnVerifyCode.setVisibility(View.GONE); // Ẩn nút Verify Code
                dialog.dismiss();
                showResetPasswordDialog(user.getEmail(), user.getSalt());

            } else {
                Toast.makeText(this, "Invalid verification code.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showResetPasswordDialog(String email, String salt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
        builder.setView(dialogView);

        EditText newPasswordInput = dialogView.findViewById(R.id.newPasswordInput);
        Button btnConfirmReset = dialogView.findViewById(R.id.btnConfirmReset);
        Button btnCancel = dialogView.findViewById(R.id.btnCancelReset);
        AlertDialog dialog = builder.create();

        btnConfirmReset.setOnClickListener(view -> {
            String newPassword = newPasswordInput.getText().toString().trim();
            if (!PasswordUtil.isStrongPassword(newPassword)) {
                Toast.makeText(this, "Password must be at least 8 characters, including uppercase, lowercase, a digit, and a special character.", Toast.LENGTH_LONG).show();
            } else {
                try {
                    userSQLiteHelper.updatePassword(email, HashUtil.hashPasswordWithSalt(newPassword, salt));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(this, "Password reset successful!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }
}