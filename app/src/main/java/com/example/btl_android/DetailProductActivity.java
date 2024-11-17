package com.example.btl_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_android.adapter.RecycleCommentAdapter;
import com.example.btl_android.dal.CartSQLiteHelper;
import com.example.btl_android.dal.CommentSQLiteHelper;
import com.example.btl_android.dal.OrderSQLiteHelper;
import com.example.btl_android.dal.ProductSQLiteHelper;
import com.example.btl_android.dal.UserSQLiteHelper;
import com.example.btl_android.model.Cart;
import com.example.btl_android.model.Comment;
import com.example.btl_android.model.Order;
import com.example.btl_android.model.OrderProduct;
import com.example.btl_android.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DetailProductActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView image;
    private FloatingActionButton fab_back, fab_addcart, fab_buy;
    private TextView price, name, desc, star, content;
    private RatingBar rating;
    private EditText comment;
    private Button btnPost;
    private RecyclerView recyclerView;
    private RecycleCommentAdapter adapter;
    private List<Comment> list;
    private CartSQLiteHelper cartSQLiteHelper;
    private CommentSQLiteHelper commentSQLiteHelper;
    private OrderSQLiteHelper orderSQLiteHelper;
    private UserSQLiteHelper userSQLiteHelper;
    private SharedPreferences sharedPreferences;
    private static final String SHARE_PRE_NAME = "mypref";
    private Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        initView();
        fab_back.setOnClickListener(this);
        fab_addcart.setOnClickListener(this);
        fab_buy.setOnClickListener(this);

        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        adapter = new RecycleCommentAdapter();
        image.setImageResource(Integer.parseInt(product.getImage()));
        name.setText(product.getName());
        price.setText(product.getPrice() + " $");
        desc.setText(product.getDescription());

        list = commentSQLiteHelper.getCommentByProductId(product.getId() + "");
        if(list.size() == 0){
            content.setText("Chưa có bình luận nào");
        } else {
            content.setText("Đã có tất cả " + list.size() + " bình luận");
        }
        star.setText(getAverageStar(list));
        Collections.reverse(list);
        adapter.setList(list);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        btnPost.setOnClickListener(this);
        String user_id = sharedPreferences.getString("id", null);
        String role = sharedPreferences.getString("role", null);
        if(role != null && role.equals("customer")){
            fab_buy.setVisibility(View.VISIBLE);
            fab_addcart.setVisibility(View.VISIBLE);
            if(bought(user_id, product.getId()+"") && !commentSQLiteHelper.commented(String.valueOf(product.getId()), user_id)){
                LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.parseColor("#FFD700"), PorterDuff.Mode.SRC_ATOP);
                rating.setVisibility(View.VISIBLE);
                comment.setVisibility(View.VISIBLE);
                btnPost.setVisibility(View.VISIBLE);
            } else {
                rating.setVisibility(View.GONE);
                comment.setVisibility(View.GONE);
                btnPost.setVisibility(View.GONE);
            }
        } else {
            fab_buy.setVisibility(View.GONE);
            fab_addcart.setVisibility(View.GONE);
            rating.setVisibility(View.GONE);
            comment.setVisibility(View.GONE);
            btnPost.setVisibility(View.GONE);
        }
    }

    private void initView() {
        image = findViewById(R.id.image);
        fab_back = findViewById(R.id.fab_back);
        fab_addcart = findViewById(R.id.fab_addcart);
        fab_buy = findViewById(R.id.fab_buy);
        price = findViewById(R.id.price);
        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);
        star = findViewById(R.id.star);
        content = findViewById(R.id.content);
        rating = findViewById(R.id.rating);
        comment = findViewById(R.id.comment);
        btnPost = findViewById(R.id.btnPost);
        recyclerView = findViewById(R.id.recyclerview);
        commentSQLiteHelper = new CommentSQLiteHelper(getApplicationContext());
        orderSQLiteHelper = new OrderSQLiteHelper(getApplicationContext());
        userSQLiteHelper = new UserSQLiteHelper(getApplicationContext());
        sharedPreferences = getSharedPreferences(SHARE_PRE_NAME, MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        if(view == btnPost){
            String user_id = sharedPreferences.getString("id", null);
            String product_id = String.valueOf(product.getId());
            String ratingStr = String.valueOf((int) rating.getRating());
            String comment1 = comment.getText().toString();
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String date1 = simpleDateFormat.format(date);
            if(comment1.isEmpty()){
                Toast.makeText(getApplicationContext(), "You haven't commented yet!", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Xác nhận comment");
                builder.setPositiveButton("POST", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        commentSQLiteHelper.addComment(user_id, product_id, ratingStr, comment1, date1);
                        //
                        String user_id = sharedPreferences.getString("id", null);
                        list = commentSQLiteHelper.getCommentByProductId(product.getId() + "");
                        Collections.reverse(list);
                        content.setText("Đã có tất cả " + list.size() + " bình luận");
                        star.setText(getAverageStar(list));
                        adapter = new RecycleCommentAdapter();
                        adapter.setList(list);
                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(adapter);
                        rating.setVisibility(View.GONE);
                        comment.setVisibility(View.GONE);
                        btnPost.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "You've commentd", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        if(view == fab_back){
            finish();
        }
        if(view == fab_addcart){
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Add to cart");
            builder.setIcon(R.drawable.ic_cart);
            View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_input, null);
            builder.setView(dialogView);
            final EditText quantity = dialogView.findViewById(R.id.quantity);
            builder.setPositiveButton("ADD CART", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(quantity.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please enter quantity", Toast.LENGTH_SHORT).show();
                    } else {
                        int q = Integer.parseInt(quantity.getText().toString());
                        cartSQLiteHelper = new CartSQLiteHelper(getApplicationContext());
                        sharedPreferences = getApplicationContext().getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
                        String user_id = sharedPreferences.getString("id", null);
                        Cart cart = cartSQLiteHelper.getCartProduct(user_id, product.getId() + "");
                        if(cart != null){
                            int new_quantity = q + cart.getQuantity();
                            cartSQLiteHelper.updateCart(cart.getId() + "", product.getId() + "", user_id, new_quantity + "");
                            Toast.makeText(getApplicationContext(), "You've add " + product.getName() + " to cart", Toast.LENGTH_SHORT).show();
                        } else {
                            cartSQLiteHelper.addCart(product.getId() + "", user_id, q + "");
                            Toast.makeText(getApplicationContext(), "You've add " + product.getName() + " to cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if(view == fab_buy){
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Buy product");
            View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_input, null);
            builder.setView(dialogView);
            final EditText quantity = dialogView.findViewById(R.id.quantity);
            builder.setPositiveButton("BUY NOW", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(quantity.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please enter quantity", Toast.LENGTH_SHORT).show();
                    } else {
                        int q = Integer.parseInt(quantity.getText().toString());
                        sharedPreferences = getApplicationContext().getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
                        String user_id = sharedPreferences.getString("id", null);
                        Cart cart = new Cart(0, product, userSQLiteHelper.getUserById(user_id), q);
                        List<Cart> list1 = new ArrayList<>();
                        list1.add(cart);
                        Intent intent = new Intent(getApplicationContext(), AddOrderActivity.class);
                        intent.putExtra("list", (Serializable) list1);
                        startActivity(intent);
                    }
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private String getAverageStar(List<Comment> list){
        int n = 0;
        if(list.size() == 0) return "--";
        for(Comment c: list){
            n += c.getRating();
        }
        double avg = 1.0 * n / list.size();
        avg = Math.round(avg * 10.0) / 10.0;
        return String.valueOf(avg);
    }

    private boolean bought(String user_id, String product_id){
        List<Order> list1 = orderSQLiteHelper.getOrderByUserId(user_id);
        for(Order order: list1){
            for(OrderProduct orderProduct: order.getOrderProducts()){
                if(product_id.equalsIgnoreCase(orderProduct.getProduct().getId() + "") && order.getStatus().equals("Done")){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String user_id = sharedPreferences.getString("id", null);
        String role = sharedPreferences.getString("role", null);
        if(role != null && role.equals("customer")){
            fab_buy.setVisibility(View.VISIBLE);
            fab_addcart.setVisibility(View.VISIBLE);
            if(bought(user_id, product.getId()+"") && !commentSQLiteHelper.commented(String.valueOf(product.getId()), user_id)){
                LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.parseColor("#FFD700"), PorterDuff.Mode.SRC_ATOP);
                rating.setVisibility(View.VISIBLE);
                comment.setVisibility(View.VISIBLE);
                btnPost.setVisibility(View.VISIBLE);
            } else {
                rating.setVisibility(View.GONE);
                comment.setVisibility(View.GONE);
                btnPost.setVisibility(View.GONE);
            }
        } else {
            fab_buy.setVisibility(View.GONE);
            fab_addcart.setVisibility(View.GONE);
            rating.setVisibility(View.GONE);
            comment.setVisibility(View.GONE);
            btnPost.setVisibility(View.GONE);
        }
    }
}