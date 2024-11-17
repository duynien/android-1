package com.example.btl_android.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.btl_android.fragment.FragmentAccount;
import com.example.btl_android.fragment.FragmentCart;
import com.example.btl_android.fragment.FragmentHome;
import com.example.btl_android.fragment.FragmentOrder;
import com.example.btl_android.fragment.FragmentVoucher;
import com.example.btl_android.model.User;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private User userLogin;
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentHome();
            case 1:
                return new FragmentCart();
            case 2:
                return new FragmentOrder();
            case 3:
                return new FragmentVoucher();
            case 4:
                return new FragmentAccount();
            default:
                return new FragmentHome();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}

