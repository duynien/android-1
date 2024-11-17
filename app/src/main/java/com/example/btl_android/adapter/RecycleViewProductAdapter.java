package com.example.btl_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.R;
import com.example.btl_android.model.Product;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewProductAdapter extends RecyclerView.Adapter<RecycleViewProductAdapter.ProductViewHolder>{
    List<Product> list;
    private ItemListener itemListener;

    public RecycleViewProductAdapter() {
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = list.get(position);
        holder.image.setImageResource(Integer.parseInt(product.getImage()));
        holder.name.setText(product.getName());
        holder.price.setText("Price: " + product.getPrice() + " $");
        holder.category.setText("Brand: " + product.getCategory());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setList(List<Product> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }
    public Product getProduct(int position){
        return list.get(position);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView name, category, price;
        public ProductViewHolder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
            category = view.findViewById(R.id.category);
            price = view.findViewById(R.id.price);
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
}
