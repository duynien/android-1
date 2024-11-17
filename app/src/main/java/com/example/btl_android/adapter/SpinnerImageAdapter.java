package com.example.btl_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.btl_android.R;

public class SpinnerImageAdapter extends BaseAdapter {
    private int[] imgs = {R.drawable.product1, R.drawable.product2, R.drawable.product3,
            R.drawable.product4, R.drawable.product5, R.drawable.product6,
            R.drawable.product7, R.drawable.product8, R.drawable.product9, R.drawable.product10};
    private Context context;

    public SpinnerImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public Object getItem(int position) {
        return imgs[position];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_spinner, viewGroup, false);
        ImageView img = item.findViewById(R.id.image);
        img.setImageResource(imgs[position]);
        return item;
    }
}
