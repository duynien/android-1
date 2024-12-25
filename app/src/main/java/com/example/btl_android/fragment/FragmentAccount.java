package com.example.btl_android.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.btl_android.LoginActivity;
import com.example.btl_android.MainActivity;
import com.example.btl_android.R;
import com.example.btl_android.dal.UserSQLiteHelper;
import com.example.btl_android.model.User;
import com.example.btl_android.util.CommonUtil;
import com.example.btl_android.util.HashUtil;
import com.example.btl_android.util.JavaMailUtil;
import com.example.btl_android.util.PasswordUtil;
import com.example.btl_android.util.RateLimiter;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FragmentAccount extends Fragment implements View.OnClickListener {
    private static final String SHARE_PRE_NAME = "mypref";
    private static final int MAX_REQUESTS_PER_MINUTE = 5;
    private static final long TIME_WINDOW_MILLIS = TimeUnit.MINUTES.toMillis(1);
    private static final long TOKEN_EXPIRATION_TIME = 5 * 60 * 1000;
    private final RateLimiter rateLimiter = new RateLimiter(MAX_REQUESTS_PER_MINUTE,
            TIME_WINDOW_MILLIS);
    private long tokenGenerationTime;
    private ImageView imgAvatar;
    private String verificationCode;
    private TextView name, username, role, email, phone, title;
    private TextView emailEditText, phoneEditText, nameEditText, usernameEditText;
    private Button btnLogin, btnLogout;
    private ImageButton btnEdit, btnPassword;
    private SharedPreferences sharedPreferences;
    private int attemptCount = 0; // Track the number of verification attempts
    private UserSQLiteHelper userSQLiteHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        sharedPreferences = getActivity().getSharedPreferences(SHARE_PRE_NAME,
                Context.MODE_PRIVATE);
        String nameLogin = sharedPreferences.getString("name", null);
        String usernameLogin = sharedPreferences.getString("username", null);
        String roleLogin = sharedPreferences.getString("role", null);
        String emailLogin = sharedPreferences.getString("email", null);
        String phoneLogin = sharedPreferences.getString("phone", null);

        if (usernameLogin == null) {
            title.setText("Welcome back, Guest");
            imgAvatar.setVisibility(View.INVISIBLE);
            btnLogout.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.INVISIBLE);
            btnPassword.setVisibility(View.INVISIBLE);
            nameEditText.setVisibility(View.INVISIBLE);
            usernameEditText.setVisibility(View.INVISIBLE);
            emailEditText.setVisibility(View.INVISIBLE);
            phoneEditText.setVisibility(View.INVISIBLE);
            name.setVisibility(View.INVISIBLE);
            username.setVisibility(View.INVISIBLE);
            email.setVisibility(View.INVISIBLE);
            phone.setVisibility(View.INVISIBLE);
        } else {
            title.setText("Welcome back, " + nameLogin);
            imgAvatar.setVisibility(View.VISIBLE);
            nameEditText.setText(nameLogin);
            usernameEditText.setText(usernameLogin);
            emailEditText.setText(emailLogin);
            phoneEditText.setText(phoneLogin);
            btnLogout.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
            btnPassword.setVisibility(View.VISIBLE);
        }

        btnLogin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        btnEdit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Update Info");

            // Inflate the custom layout for the dialog
            View dialogView = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_edit_account, null);
            builder.setView(dialogView);

            EditText dialogEmailEditText = dialogView.findViewById(R.id.dialogEmail);
            EditText dialogPhoneEditText = dialogView.findViewById(R.id.dialogPhone);

            // Pre-fill with current email and phone
            dialogEmailEditText.setText(sharedPreferences.getString("email", ""));
            dialogPhoneEditText.setText(sharedPreferences.getString("phone", ""));

            Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);
            Button btnCancel = dialogView.findViewById(R.id.btnCancel);

            // Create the dialog instance before setting listeners
            AlertDialog dialog = builder.create();

            // Set the Submit button action
            btnSubmit.setOnClickListener(v1 -> {
                String updatedEmail = dialogEmailEditText.getText().toString();
                String updatedPhone = dialogPhoneEditText.getText().toString();

                if (!CommonUtil.isValidEmail(updatedEmail)) {
                    Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!CommonUtil.isValidPhoneNumber(updatedPhone)) {
                    Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendVerificationCode(updatedEmail);
                showVerificationDialog();

                try {
                    userSQLiteHelper.updateEmailAndPhoneUser(updatedEmail, updatedPhone,
                            usernameLogin);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Failed to update user info", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                // Update shared preferences with the new values
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", updatedEmail);
                editor.putString("phone", updatedPhone);
                editor.apply();

                // Update the EditText fields in the fragment with new values
                emailEditText.setText(updatedEmail); // Update email EditText
                phoneEditText.setText(updatedPhone); // Update phone EditText

                // Dismiss the dialog
                dialog.dismiss();
            });

            // Set the Cancel button action
            btnCancel.setOnClickListener(v12 -> dialog.dismiss());

            // Show the dialog
            dialog.show();
        });

        btnPassword.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Update Password");

            // Inflate the custom layout for the dialog
            View dialogView = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_reset_password, null);
            builder.setView(dialogView);
            EditText dialogEmailEditText = dialogView.findViewById(R.id.newPasswordInput);
            Button btnSubmit = dialogView.findViewById(R.id.btnConfirmReset);
            Button btnCancel = dialogView.findViewById(R.id.btnCancelReset);

            // Create the dialog instance before setting listeners
            AlertDialog dialog = builder.create();

            btnSubmit.setOnClickListener(view1 -> {
                String newPassword = dialogEmailEditText.getText().toString();
                if (newPassword.isEmpty()) {
                    Toast.makeText(getContext(), "Password cannot be empty", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                try {
                    User user = userSQLiteHelper.getUserByEmail(emailLogin);
                    userSQLiteHelper.updatePassword(emailLogin, HashUtil.hashPasswordWithSalt(newPassword, user.getSalt()));
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                dialog.dismiss();
            });

            btnCancel.setOnClickListener(view1 -> dialog.dismiss());
            dialog.show();
        });
    }

    @SuppressLint("DefaultLocale")
    private void sendVerificationCode(String email) {
        Random random = new Random();
        verificationCode = String.format("%06d", random.nextInt(1000000));
        tokenGenerationTime = System.currentTimeMillis();
        JavaMailUtil.sendVerificationEmail(email, verificationCode);
        Toast.makeText(getContext(), "Verification code sent to your email", Toast.LENGTH_SHORT)
                .show();
    }

    private void showVerificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                Toast.makeText(getContext(),
                        "Verification code has expired. Please request a new one.",
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else if (enteredCode.equals(verificationCode)) {
                attemptCount = 0;
                dialog.dismiss();
            } else {
                attemptCount++;
                if (attemptCount >= 3) {
                    Toast.makeText(getContext(), "Too many failed attempts. Returning to login.",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Incorrect code. Attempt " + attemptCount + "/3",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Handle Cancel button click
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
    private void initView(View view) {
        name = view.findViewById(R.id.nameLabel);
        username = view.findViewById(R.id.usernameLabel);
        email = view.findViewById(R.id.emailLabel);
        phone = view.findViewById(R.id.phoneLabel);
        nameEditText = view.findViewById(R.id.name);
        usernameEditText = view.findViewById(R.id.username);
        emailEditText = view.findViewById(R.id.email);
        phoneEditText = view.findViewById(R.id.phone);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnPassword = view.findViewById(R.id.btnPassword);
        title = view.findViewById(R.id.title);
        imgAvatar = view.findViewById(R.id.imageView);
        userSQLiteHelper = new UserSQLiteHelper(getContext());
    }

    @Override
    public void onClick(View view) {
        if (view == btnLogin) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
        if (view == btnLogout) {
            new AlertDialog.Builder(getContext()).setTitle("LOGOUT")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("LOGOUT", (dialog, which) -> {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }).setNegativeButton("CANCEL", null).show();
        }
    }
}
