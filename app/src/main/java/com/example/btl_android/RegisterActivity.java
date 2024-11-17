package com.example.btl_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_android.dal.UserSQLiteHelper;
import com.example.btl_android.model.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, username, password, repass;
    private Button btnRegister, btCancel;
    private UserSQLiteHelper userSQLiteHelper;
    private final int REQUEST_CODE = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        btCancel.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }
    private void initView(){
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repass = findViewById(R.id.repass);
        btnRegister = findViewById(R.id.btnRegister);
        btCancel = findViewById(R.id.btnCancel);
        userSQLiteHelper = new UserSQLiteHelper(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnRegister){
            if(name.getText().toString().isEmpty() || username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || repass.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "Required to input all fields!", Toast.LENGTH_SHORT).show();
            } else {
                if(password.getText().toString().equals(repass.getText().toString())){
                    if(PasswordUtil.isStrongPassword(password.getText().toString())){
                        User user = new User(name.getText().toString(), username.getText().toString(), password.getText().toString(), "customer");
                        if(userSQLiteHelper.isExisted(user.getUsername())){
                            Toast.makeText(getApplicationContext(), "username is alrealdy existed!", Toast.LENGTH_SHORT).show();
                        } else {
                            String rawPassword = user.getPassword();
                            String encodePassword = "";
                            try {
                                encodePassword = HashUtil.sha256(rawPassword);
                            } catch (Exception e) {
                                System.out.println("Hashing error: " + e.getMessage());
                                e.printStackTrace();
                            }
                            user.setPassword(encodePassword);
                            userSQLiteHelper.addUser(user);
                            Intent intent = new Intent();
                            user.setPassword(rawPassword);
                            intent.putExtra("user", user);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Password is not enough strong!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Password and confirm password doesn't match!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        if(view == btCancel){
            setResult(RESULT_CANCELED, null);
            finish();
        }
    }
}