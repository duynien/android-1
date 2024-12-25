package com.example.btl_android.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.R;
import com.example.btl_android.adapter.RecycleVoucherAdapter;
import com.example.btl_android.dal.VoucherSQLiteHelper;
import com.example.btl_android.model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class FragmentVoucher extends Fragment {
    private RecyclerView recyclerView;
    private VoucherSQLiteHelper voucherSQLiteHelper;
    private TextView title;
    private RecycleVoucherAdapter adapter;
    private List<Voucher> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voucher, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        adapter = new RecycleVoucherAdapter(getContext());
        list = voucherSQLiteHelper.getAllVoucher();

        if (list.isEmpty()) {
            title.setVisibility(View.VISIBLE);
            title.setText("No voucher available");
        }

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView1);
        voucherSQLiteHelper = new VoucherSQLiteHelper(getContext());
        title = view.findViewById(R.id.voucher_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        list = voucherSQLiteHelper.getAllVoucher();
        adapter.setList(list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
}
