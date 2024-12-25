package com.example.btl_android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.R;
import com.example.btl_android.model.Order;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RecycleOrderAdapter extends RecyclerView.Adapter<RecycleOrderAdapter.OrderViewHolder>{
    private List<Order> list;
    private ItemListener itemListener;

    public RecycleOrderAdapter() {
        this.list = new ArrayList<>();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setList(List<Order> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }
    public Order getOrder(int position){
        return list.get(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = list.get(position);
        holder.date.setText(order.getDate());
        holder.total_price.setText(formatNumber(order.getTotal_price()) + " $");

        if (order.getStatus().equalsIgnoreCase("Done")) {
            holder.status.setText(order.getStatus());
            holder.status.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            holder.status.setText("Processing");
            // set color for status processing is orange
            holder.status.setTextColor(Color.parseColor("#FF5722"));
        }

//        if(order.getStatus().equalsIgnoreCase("Done")){
//            holder.date.setBackgroundColor(Color.parseColor("#4CAF50"));
//            holder.total_price.setBackgroundColor(Color.parseColor("#4CAF50"));
//        } else {
//            holder.date.setBackgroundColor(Color.parseColor("#FF5722"));
//            holder.total_price.setBackgroundColor(Color.parseColor("#FF5722"));
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView date, total_price, status;
        private LinearLayout item_order;
        public OrderViewHolder(@NonNull View view) {
            super(view);
            date = view.findViewById(R.id.date);
            total_price = view.findViewById(R.id.total_price);
            status = view.findViewById(R.id.status);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemListener != null){
                itemListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
    public interface ItemListener {
        void onItemClick(View view, int position);
    }
    public String formatNumber(int n){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedNumber = decimalFormat.format(n);
        return formattedNumber;
    }
}
