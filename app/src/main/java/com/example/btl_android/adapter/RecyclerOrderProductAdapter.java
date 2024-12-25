package com.example.btl_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.R;
import com.example.btl_android.model.OrderProduct;

import java.util.ArrayList;
import java.util.List;

public class RecyclerOrderProductAdapter extends
        RecyclerView.Adapter<RecyclerOrderProductAdapter.OrderProductViewHolder> {
    private List<OrderProduct> list;

    public RecyclerOrderProductAdapter() {
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_order, parent, false);
        return new OrderProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductViewHolder holder, int position) {
        OrderProduct orderProduct = list.get(position);
        holder.image.setImageResource(Integer.parseInt(orderProduct.getProduct().getImage()));
        holder.name.setText(orderProduct.getProduct().getName());
        holder.price.setText("Price: " + orderProduct.getProduct().getPrice() + " $");
        holder.amount.setText("Amount: " + orderProduct.getQuantity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<OrderProduct> list) {
        this.list = list;
    }

    public class OrderProductViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price, amount;

        public OrderProductViewHolder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
            price = view.findViewById(R.id.price);
            amount = view.findViewById(R.id.amount);
        }
    }
}
