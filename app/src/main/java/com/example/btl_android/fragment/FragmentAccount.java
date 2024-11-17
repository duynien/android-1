package com.example.btl_android.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.btl_android.LoginActivity;
import com.example.btl_android.MainActivity;
import com.example.btl_android.R;
import com.example.btl_android.dal.CartSQLiteHelper;
import com.example.btl_android.model.User;

public class FragmentAccount extends Fragment implements View.OnClickListener{
    private TextView name, username, role;
    private Button btnLogin, btnLogout;
    private User userLogin;
    private SharedPreferences sharedPreferences;
    private static final String SHARE_PRE_NAME = "mypref";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        sharedPreferences = getActivity().getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        String nameLogin = sharedPreferences.getString("name", null);
        String usernameLogin = sharedPreferences.getString("username", null);
        String roleLogin = sharedPreferences.getString("role", null);
        System.out.println(nameLogin + " " + usernameLogin + " " + roleLogin);

        if(usernameLogin == null){
            btnLogout.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
        } else {
            name.setText("Name: " + nameLogin);
            username.setText("Username: " + usernameLogin);
            role.setText("Role: " + roleLogin);
            btnLogout.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
        }
        btnLogin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    private void initView(View view) {
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.username);
        role = view.findViewById(R.id.role);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    @Override
    public void onClick(View view) {
        if(view == btnLogin){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
        if(view == btnLogout){
            new AlertDialog.Builder(getContext())
                    .setTitle("LOGOUT")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("CANCEL", null)
                    .show();

        }
    }
}
