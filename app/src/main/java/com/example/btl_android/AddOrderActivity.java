package com.example.btl_android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.adapter.RecycleCartOrderAdapter;
import com.example.btl_android.dal.CartSQLiteHelper;
import com.example.btl_android.dal.OrderProductSQLiteHelper;
import com.example.btl_android.dal.OrderSQLiteHelper;
import com.example.btl_android.dal.VoucherSQLiteHelper;
import com.example.btl_android.model.Cart;
import com.example.btl_android.model.Voucher;
import com.example.btl_android.util.CommonUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String SHARE_PRE_NAME = "mypref";
    private RecyclerView recyclerView;
    private EditText mobile, delivery_address, voucher_code;
    private Spinner spinnerProvince;
    private String selectedProvince;
    private TextView price, discount, total_price;
    private Button btnConfirm, btnCancel;
    private OrderSQLiteHelper orderSQLiteHelper;
    private OrderProductSQLiteHelper orderProductSQLiteHelper;
    private CartSQLiteHelper cartSQLiteHelper;
    private VoucherSQLiteHelper voucherSQLiteHelper;
    private SharedPreferences sharedPreferences;
    private RecycleCartOrderAdapter adapter;
    private List<Cart> list;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        initView();
        btnCancel.setOnClickListener(this);
        String id = sharedPreferences.getString("id", null);


        spinnerProvince = findViewById(R.id.spinnerProvince);
        ArrayAdapter<CharSequence> adapterProvinces = ArrayAdapter.createFromResource(this,
                R.array.provinces, android.R.layout.simple_spinner_item);
        adapterProvinces.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(adapterProvinces);

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedProvince = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedProvince = null;
            }
        });

        if (id != null) {
            adapter = new RecycleCartOrderAdapter(getApplicationContext());
            Intent intent = getIntent();
            list = (List<Cart>) intent.getSerializableExtra("list");
            adapter.setList(list);
            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),
                    RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            btnConfirm.setOnClickListener(this);
            price.setText(formatNumber(getTotalPrice(list)));
            total_price.setText(formatNumber(getTotalPrice(list)) + " $");
            discount.setText("0");
            voucher_code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String code = voucher_code.getText().toString();
                    Voucher voucher = voucherSQLiteHelper.getVoucherByCode(code);
                    System.out.println(voucher);
                    if (voucher == null) {
                        discount.setText("0");
                        total_price.setText(formatNumber(getTotalPrice(list)) + " $");
                    } else{
                        System.out.println(voucher.getStart());
                        System.out.println(voucher.getEnd());
                        if(isAvailable(voucher.getStart(), voucher.getEnd())){
                            int x = Integer.parseInt(voucher.getPercentage());
                            int discount1 = getTotalPrice(list) * x / 100;
                            discount.setText(formatNumber(discount1));
                            total_price.setText((formatNumber(getTotalPrice(list) - discount1)) + " $");
                            Toast.makeText(getApplicationContext(), "Applied voucher!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        mobile = findViewById(R.id.mobile);
        delivery_address = findViewById(R.id.delivery_address);
        voucher_code = findViewById(R.id.voucher_code);
        price = findViewById(R.id.price);
        discount = findViewById(R.id.discount);
        total_price = findViewById(R.id.total_price);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        sharedPreferences = getApplicationContext().getSharedPreferences(SHARE_PRE_NAME,
                Context.MODE_PRIVATE);
        orderSQLiteHelper = new OrderSQLiteHelper(getApplicationContext());
        orderProductSQLiteHelper = new OrderProductSQLiteHelper(getApplicationContext());
        cartSQLiteHelper = new CartSQLiteHelper(getApplicationContext());
        voucherSQLiteHelper = new VoucherSQLiteHelper(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        if (view == btnCancel) {
            finish();
        }
        if (view == btnConfirm) {
            String mobile1 = mobile.getText().toString();
            String delivery_address1 = delivery_address.getText().toString();
            String discount1 = discount.getText().toString();
            if (mobile1.isEmpty() || delivery_address1.isEmpty() || discount1.isEmpty() || selectedProvince == null) {
                Toast.makeText(getApplicationContext(), "All fields are required!",
                        Toast.LENGTH_SHORT).show();
            } else {

                String finalDelivery_address = delivery_address1 + ", " + selectedProvince;
                if (CommonUtil.isValidPhoneNumber(mobile1)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Confirm order");
                    builder.setMessage("Are you sure to confirm order?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sharedPreferences = getApplicationContext().getSharedPreferences(
                                    SHARE_PRE_NAME, Context.MODE_PRIVATE);
                            String user_id = sharedPreferences.getString("id", null);
                            StringBuilder total_price1 = new StringBuilder(
                                    total_price.getText().toString());
                            total_price1 = new StringBuilder(
                                    total_price1.substring(0, total_price1.length() - 2));
                            String[] data = total_price1.toString().split(",");
                            total_price1 = new StringBuilder();
                            for (String s : data) total_price1.append(s.trim());
                            Date date = new Date();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                                    "HH:mm dd/MM/yyyy");
                            String dateStr = simpleDateFormat.format(date);
                            long order_id = orderSQLiteHelper.addOrder(user_id,
                                    total_price1.toString(), mobile1, finalDelivery_address,
                                    "notDone", dateStr);
                            for (Cart c : list) {
                                orderProductSQLiteHelper.addOrderProduct(String.valueOf(order_id),
                                        String.valueOf(c.getProduct().getId()),
                                        String.valueOf(c.getQuantity()));
                            }
//                            cartSQLiteHelper.deleteCart(user_id);
                            Toast.makeText(getApplicationContext(), "You've confirmed order!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid phone number!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private int getTotalPrice(List<Cart> list) {
        int sum = 0;
        for (Cart c : list) {
            sum += Integer.parseInt(c.getProduct().getPrice()) * c.getQuantity();
        }
        return sum;
    }
    public String formatNumber(int n) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(n);
    }

    public static boolean isAvailable(String s1, String s2) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            Date currentDate = new Date();
            Date start = format.parse(s1);
            Date end = format.parse("23:59 " + s2.trim());
            return !currentDate.before(start) && !currentDate.after(end);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}