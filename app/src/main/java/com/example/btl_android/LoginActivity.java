package com.example.btl_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.dal.UserSQLiteHelper;
import com.example.btl_android.model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username, password;
    private Button btnLogin, btnRegister;
    private UserSQLiteHelper userSQLiteHelper;
    private final int REQUEST_CODE = 9999;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }
    private void initView(){
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        userSQLiteHelper = new UserSQLiteHelper(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnLogin){
            if(username.getText().toString() != null && password.getText().toString() != null){
                String encodePassword = "";
                try {
                    String originalText = password.getText().toString();
                    encodePassword = HashUtil.sha256(originalText);
                } catch (Exception e) {
                    System.out.println("Hashing error: " + e.getMessage());
                    e.printStackTrace();
                }
                User account = userSQLiteHelper.getAccount(username.getText().toString(), encodePassword);
                if(account == null){
                    Toast.makeText(getApplicationContext(), "Wrong username or password!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("account", account);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Require to input all fields!", Toast.LENGTH_SHORT).show();
            }
        }
        if(view == btnRegister){
            // su dung intent nay nham muc dich nhan lai du lieu do intent kia gui ve
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }
    // nhan du lieu do register activity gui ve
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data == null){
                Toast.makeText(getApplicationContext(), "You're already cancel register!", Toast.LENGTH_SHORT).show();
            } else {
                user = (User) data.getSerializableExtra("user");
                username.setText(user.getUsername());
                password.setText(user.getPassword());
            }
        }
    }
}