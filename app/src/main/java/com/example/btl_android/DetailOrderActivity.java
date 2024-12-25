package com.example.btl_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.adapter.RecyclerOrderProductAdapter;
import com.example.btl_android.dal.OrderSQLiteHelper;
import com.example.btl_android.model.Cart;
import com.example.btl_android.model.Order;
import com.example.btl_android.model.OrderProduct;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DetailOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView name, mobile, address, price, discount, total_price, status;
    private Button btnConfirm;
    private RecyclerView recyclerView;
    private RecyclerOrderProductAdapter adapter;
    private SharedPreferences sharedPreferences;
    private OrderSQLiteHelper orderSQLiteHelper;
    private static final String SHARE_PRE_NAME = "mypref";
    private List<OrderProduct> list;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        initView();
        btnConfirm.setOnClickListener(this);
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");
        List<OrderProduct> orderProducts = order.getOrderProducts();
        adapter = new RecyclerOrderProductAdapter();
        adapter.setList(orderProducts);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        name.setText(order.getUser().getName());
        mobile.setText(order.getMobile());
        address.setText(order.getAddress());
        price.setText(String.valueOf(getTongTien(orderProducts)));
        discount.setText(String.valueOf(getTongTien(orderProducts) - order.getTotal_price()));
        total_price.setText(order.getTotal_price() + " $");
        if(order.getStatus().equalsIgnoreCase("Done")){
            status.setText("Đơn hàng đã xong");
        } else {
            status.setText("Đang xử lý");
        }
        sharedPreferences = getApplicationContext().getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", null);
        if(role != null && role.equalsIgnoreCase("admin")){
            btnConfirm.setVisibility(View.VISIBLE);
        } else {
            btnConfirm.setVisibility(View.GONE);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết đơn hàng");
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initView() {
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        price = findViewById(R.id.price);
        discount = findViewById(R.id.discount);
        total_price = findViewById(R.id.total_price);
        status = findViewById(R.id.status);
        btnConfirm = findViewById(R.id.btnConfirm);
        recyclerView = findViewById(R.id.recyclerView);
        orderSQLiteHelper = new OrderSQLiteHelper(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        if(view == btnConfirm){
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Thông báo xác nhận");
            builder.setMessage("Bạn chắc chắn muồn xác nhận đơn hàng?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    orderSQLiteHelper.updateOrder(order.getId() + "", order.getUser().getId() + "", order.getTotal_price()+ "", order.getMobile(), order.getAddress(), "Done", order.getDate());
                    Toast.makeText(getApplicationContext(), "You've confirmed order", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private int getTongTien(List<OrderProduct> list){
        int sum = 0;
        for(OrderProduct o: list){
            if (o.getProduct() == null) {
                continue;
            }
            sum += Integer.parseInt(o.getProduct().getPrice()) * o.getQuantity();
        }
        return sum;
    }

}