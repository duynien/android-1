package com.example.btl_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.R;
import com.example.btl_android.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class RecycleCommentAdapter extends RecyclerView.Adapter<RecycleCommentAdapter.CommentViewHolder>{
    private List<Comment> list;

    public RecycleCommentAdapter() {
        this.list = new ArrayList<>();
    }

    public void setList(List<Comment> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = list.get(position);
        holder.username.setText(comment.getUser().getUsername());
        int rate = comment.getRating();
        String s = "";
        for(int i=0; i<rate; i++){
            s += "*";
        }
        holder.star.setText(s);
        holder.date.setText(comment.getDate());
        holder.comment.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView username, star, date, comment;
        public CommentViewHolder(@NonNull View view) {
            super(view);
            username = view.findViewById(R.id.username);
            star = view.findViewById(R.id.star);
            date = view.findViewById(R.id.date);
            comment = view.findViewById(R.id.comment);
        }
    }
}
