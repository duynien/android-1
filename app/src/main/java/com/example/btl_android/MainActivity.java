package com.example.btl_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.btl_android.adapter.ViewPagerAdapter;
import com.example.btl_android.dal.UserSQLiteHelper;
import com.example.btl_android.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String SHARE_PRE_NAME = "mypref";
    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    private FloatingActionButton fab_product, fab_voucher;
    private User userLogin;
    private SharedPreferences sharedPreferences;
    private UserSQLiteHelper userSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        fab_product.setOnClickListener(this);
        fab_voucher.setOnClickListener(this);
        sharedPreferences = getSharedPreferences(SHARE_PRE_NAME, MODE_PRIVATE);
        String role = sharedPreferences.getString("role", null);
        if (role != null && role.equalsIgnoreCase("admin")) {
            fab_product.setVisibility(View.VISIBLE);
            fab_voucher.setVisibility(View.VISIBLE);
        } else {
            fab_product.setVisibility(View.INVISIBLE);
            fab_voucher.setVisibility(View.INVISIBLE);
        }
        Intent intent = getIntent();
        userLogin = (User) intent.getSerializableExtra("account");
        if (userLogin != null) {
            if (userLogin.getRole().equalsIgnoreCase("admin")) {
                fab_product.setVisibility(View.VISIBLE);
                fab_voucher.setVisibility(View.VISIBLE);
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("id", userLogin.getId() + "");
            editor.putString("name", userLogin.getName());
            editor.putString("username", userLogin.getUsername());
            editor.putString("role", userLogin.getRole());
            editor.putString("email", userLogin.getEmail());
            editor.putString("phone", userLogin.getPhone());
            editor.apply();
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigationView.getMenu().findItem(R.id.mHome).setChecked(true);
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.mCart).setChecked(true);
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.mOrder).setChecked(true);
                        break;
                    case 3:
                        navigationView.getMenu().findItem(R.id.mVoucher).setChecked(true);
                        break;
                    case 4:
                        navigationView.getMenu().findItem(R.id.mAccount).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.mHome) {
                            viewPager.setCurrentItem(0);
                        } else if (id == R.id.mCart) {
                            viewPager.setCurrentItem(1);
                        } else if (id == R.id.mOrder) {
                            viewPager.setCurrentItem(2);
                        } else if (id == R.id.mVoucher) {
                            viewPager.setCurrentItem(3);
                        } else if (id == R.id.mAccount) {
                            viewPager.setCurrentItem(4);
                        }
                        return true;
                    }
                });
    }

    private void initView() {
        navigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.viewPager);
        fab_product = findViewById(R.id.fab_product);
        fab_voucher = findViewById(R.id.fab_voucher);
        fab_product.setVisibility(View.INVISIBLE);
        fab_voucher.setVisibility(View.INVISIBLE);
        userSQLiteHelper = new UserSQLiteHelper(this);
    }

    @Override
    public void onClick(View view) {
        if (view == fab_product) {
            Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
            startActivity(intent);
        }
        if (view == fab_voucher) {
            Intent intent = new Intent(MainActivity.this, AddVoucherActivity.class);
            startActivity(intent);
        }
    }
}