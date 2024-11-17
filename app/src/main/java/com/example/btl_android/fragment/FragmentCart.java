package com.example.btl_android.fragment;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.AddOrderActivity;
import com.example.btl_android.R;
import com.example.btl_android.adapter.RecycleCartAdapter;
import com.example.btl_android.dal.CartSQLiteHelper;
import com.example.btl_android.model.Cart;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

public class FragmentCart extends Fragment implements View.OnClickListener, RecycleCartAdapter.ItemListener{
    private RecyclerView recyclerView;
    private TextView total_price;
    private Button btnBuy;
    private RecycleCartAdapter adapter;
    private CartSQLiteHelper cartSQLiteHelper;
    private SharedPreferences sharedPreferences;
    private static final String SHARE_PRE_NAME = "mypref";
    private List<Cart> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        sharedPreferences = getActivity().getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        if(id != null){
            adapter = new RecycleCartAdapter(getContext());
            list = cartSQLiteHelper.getCart(id);
            if(list.size() == 0){
                btnBuy.setVisibility(View.INVISIBLE);
                total_price.setText("Chưa có sản phẩm nào!");
            } else {
                adapter.setList(list);
                total_price.setText("Total price: " + formatNumber(getTotalPrice(list)) + " $");
            }
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            adapter.setItemListener(this);
        }
        btnBuy.setOnClickListener(this);
        total_price.setOnClickListener(this);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        total_price = view.findViewById(R.id.total_price);
        btnBuy = view.findViewById(R.id.btnBuy);
        cartSQLiteHelper = new CartSQLiteHelper(getContext());
        sharedPreferences = getActivity().getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        if(id == null){
            btnBuy.setVisibility(View.INVISIBLE);
        } else {
            btnBuy.setVisibility(View.VISIBLE);
        }
    }

    private int getTotalPrice(List<Cart> list){
        int sum = 0;
        for(Cart c: list){
            sum += Integer.parseInt(c.getProduct().getPrice()) * c.getQuantity();
        }
        return sum;
    }

    @Override
    public void onClick(View view) {
        if(view == btnBuy){
            Intent intent = new Intent(getActivity(), AddOrderActivity.class);
            intent.putExtra("list", (Serializable) list);
            startActivity(intent);
        }
        if(view == total_price){
            copyToClipboard(total_price.getText().toString());
            Toast.makeText(getContext(), "Total price copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = getActivity().getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        if(id != null){
            adapter = new RecycleCartAdapter(getContext());
            list = cartSQLiteHelper.getCart(id);
            if(list.size() == 0){
                btnBuy.setVisibility(View.INVISIBLE);
                total_price.setText("Chưa có sản phẩm nào!");
            } else {
                btnBuy.setVisibility(View.VISIBLE);
                total_price.setText("Total price: " + formatNumber(getTotalPrice(list)) + " $");
            }
            adapter.setList(list);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            adapter.setItemListener(this);
        }
    }

    @Override
    public void onButtonRemoveClick(View view, int position) {
        Cart cart = adapter.getItem(position);
        new AlertDialog.Builder(getContext())
                .setTitle("Remove Item")
                .setMessage("Are you sure you want to remove?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cartSQLiteHelper = new CartSQLiteHelper(getContext());
                        cartSQLiteHelper.deleteCartById(cart.getId());
                        list.remove(position);
                        adapter.notifyItemRemoved(position);

                        // Update total price
                        int tp = getTotalPrice(list);

                        if(tp == 0){
                            total_price.setText("Chưa có sản phẩm nào!");
                            btnBuy.setVisibility(View.INVISIBLE);
                        } else {
                            btnBuy.setVisibility(View.VISIBLE);
                            total_price.setText("Total price: " + formatNumber(tp) + " $");
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    public String formatNumber(int n){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(n);
        return formattedNumber;
    }
    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("total_price", text);
        clipboard.setPrimaryClip(clip);
    }
}
