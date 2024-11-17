package com.example.btl_android.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.MainActivity;
import com.example.btl_android.R;
import com.example.btl_android.dal.CartSQLiteHelper;
import com.example.btl_android.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class RecycleCartAdapter extends RecyclerView.Adapter<RecycleCartAdapter.CartViewHolder>{
    private List<Cart> list;
    private Context context;
    private CartSQLiteHelper cartSQLiteHelper;
    private ItemListener itemListener;

    public RecycleCartAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    public Cart getItem(int position){
        return list.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cart cart = list.get(position);
        System.out.println(cart);
        holder.image.setImageResource(Integer.parseInt(cart.getProduct().getImage()));
        holder.name.setText(cart.getProduct().getName());
        holder.price.setText("Price: " + cart.getProduct().getPrice() + " $");
        holder.amount.setText("Quantity: " + cart.getQuantity());
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemListener != null){
                    itemListener.onButtonRemoveClick(view, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Cart> list) {
        this.list = list;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price, amount;
        Button btnRemove;
        public CartViewHolder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
            price = view.findViewById(R.id.price);
            amount = view.findViewById(R.id.amount);
            btnRemove = view.findViewById(R.id.btnRemove);
        }
    }

    public interface ItemListener {
        void onButtonRemoveClick(View view, int position);
    }
}
