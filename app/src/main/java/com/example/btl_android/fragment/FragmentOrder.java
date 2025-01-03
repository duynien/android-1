package com.example.btl_android.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.btl_android.DetailOrderActivity;
import com.example.btl_android.R;
import com.example.btl_android.adapter.RecycleOrderAdapter;
import com.example.btl_android.dal.OrderSQLiteHelper;
import com.example.btl_android.model.Order;

import java.util.Collections;
import java.util.List;

public class FragmentOrder extends Fragment implements RecycleOrderAdapter.ItemListener {
    private static final String SHARE_PRE_NAME = "mypref";
    private RecyclerView recyclerView;
    private RecycleOrderAdapter adapter;
    private OrderSQLiteHelper orderSQLiteHelper;
    private List<Order> list = null;
    private SharedPreferences sharedPreferences;
    private TextView tvNoOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        loadOrders();
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        tvNoOrders = view.findViewById(R.id.tvNoOrders);
        adapter = new RecycleOrderAdapter();
        orderSQLiteHelper = new OrderSQLiteHelper(getContext());
        sharedPreferences = getActivity().getSharedPreferences(SHARE_PRE_NAME,
                Context.MODE_PRIVATE);
    }

    private void loadOrders() {
        String id = sharedPreferences.getString("id", null);
        String role = sharedPreferences.getString("role", null);
        if (role != null && role.equalsIgnoreCase("customer")) {
            list = orderSQLiteHelper.getOrderByUserId(id);
        } else if (role != null && role.equalsIgnoreCase("admin")) {
            list = orderSQLiteHelper.getAll();
        }

        if (list == null) {
            tvNoOrders.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else if (list.isEmpty()) {
            tvNoOrders.setVisibility(View.VISIBLE);
            tvNoOrders.setText("No orders");
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoOrders.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Collections.reverse(list);
            adapter.setList(list);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(),
                    RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            adapter.setItemListener(this);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Order order = adapter.getOrder(position);
        Intent intent = new Intent(getContext(), DetailOrderActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }
}