package com.example.btl_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.R;
import com.example.btl_android.dal.CartSQLiteHelper;
import com.example.btl_android.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class RecycleCartOrderAdapter extends RecyclerView.Adapter<RecycleCartOrderAdapter.CartViewHolder>{
    private List<Cart> list;
    private Context context;
    private CartSQLiteHelper cartSQLiteHelper;

    public RecycleCartOrderAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_order, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = list.get(position);
        System.out.println(cart);
        holder.image.setImageResource(Integer.parseInt(cart.getProduct().getImage()));
        holder.name.setText(cart.getProduct().getName());
        holder.price.setText("Price: " + cart.getProduct().getPrice() + " $");
        holder.amount.setText("Quantity: " + cart.getQuantity());
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
        public CartViewHolder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
            price = view.findViewById(R.id.price);
            amount = view.findViewById(R.id.amount);
        }
    }
}
