package com.example.btl_android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.R;
import com.example.btl_android.adapter.RecycleVoucherAdapter;
import com.example.btl_android.dal.VoucherSQLiteHelper;
import com.example.btl_android.model.Voucher;

import java.util.List;

public class FragmentVoucher extends Fragment {
    private RecyclerView recyclerView;
    private VoucherSQLiteHelper voucherSQLiteHelper;
    private RecycleVoucherAdapter adapter;
    private List<Voucher> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voucher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        adapter = new RecycleVoucherAdapter(getContext());
        list = voucherSQLiteHelper.getAllVoucher();
        System.out.println(list);
        adapter.setList(list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView1);
        voucherSQLiteHelper = new VoucherSQLiteHelper(getContext());
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
