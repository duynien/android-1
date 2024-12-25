package com.example.btl_android;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_android.adapter.SpinnerImageAdapter;
import com.example.btl_android.dal.ProductSQLiteHelper;
import com.example.btl_android.model.Product;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {
    private final int[] imgs = {R.drawable.product1, R.drawable.product2, R.drawable.product3, R.drawable.product4, R.drawable.product5, R.drawable.product6, R.drawable.product7, R.drawable.product8, R.drawable.product9, R.drawable.product10};
    private Spinner image, category;
    private EditText name, price, description;
    private Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initView();
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initView() {
        image = findViewById(R.id.image);
        SpinnerImageAdapter adapter = new SpinnerImageAdapter(this);
        image.setAdapter(adapter);
        category = findViewById(R.id.category);
        category.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinner1,
                getResources().getStringArray(R.array.category)));
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        btnAdd = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
    }

    @Override
    public void onClick(View view) {
        if (view == btnAdd) {
            String imageP = image.getSelectedItem().toString();
            System.out.println(imageP);
            System.out.println(Integer.parseInt(imageP));
            String nameP = name.getText().toString();
            String priceP = price.getText().toString();
            String descriptionP = description.getText().toString();
            String categoryP = category.getSelectedItem().toString();
            if (!nameP.isEmpty() && !priceP.isEmpty() && !descriptionP.isEmpty()) {
                Product product = new Product(imageP, nameP, priceP, descriptionP, categoryP);
                ProductSQLiteHelper db = new ProductSQLiteHelper(this);
                db.addProduct(product);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "All fields are required!",
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (view == btnCancel) {
            finish();
        }
    }
}