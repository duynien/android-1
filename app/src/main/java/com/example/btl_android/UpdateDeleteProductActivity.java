package com.example.btl_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class UpdateDeleteProductActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner image, category;
    private EditText name, price, description;
    private Button btnUpdate, btnRemove, btnCancel;
    private Product product;
    private int[] imgs = {R.drawable.product1, R.drawable.product2, R.drawable.product3,
            R.drawable.product4, R.drawable.product5, R.drawable.product6,
            R.drawable.product7, R.drawable.product8, R.drawable.product9, R.drawable.product10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_product);
        initView();
        btnUpdate.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        name.setText(product.getName());
        price.setText(product.getPrice());
        description.setText(product.getDescription());
        int p = 0;
        for(int i=0; i < imgs.length; i++){
            if(Integer.parseInt(product.getImage()) == imgs[i]){
                p = i;
                break;
            }
        }
        image.setSelection(p);
        p = 0;
        for(int i=0; i<category.getCount(); i++){
            if(category.getItemAtPosition(i).toString().equalsIgnoreCase(product.getCategory())){
                p = i;
                break;
            }
        }
        category.setSelection(p);
    }

    private void initView() {
        image = findViewById(R.id.image);
        SpinnerImageAdapter adapter = new SpinnerImageAdapter(this);
        image.setAdapter(adapter);

        category = findViewById(R.id.category);
        category.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinner1, getResources().getStringArray(R.array.category)));
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnRemove = findViewById(R.id.btnRemove);
        btnCancel = findViewById(R.id.btnCancel);
    }

    @Override
    public void onClick(View view) {
        ProductSQLiteHelper db = new ProductSQLiteHelper(this);
        if(view == btnUpdate){
            String imageP = image.getSelectedItem().toString();
            String nameP = name.getText().toString();
            String priceP = price.getText().toString();
            String descriptionP = description.getText().toString();
            String categoryP = category.getSelectedItem().toString();
            if(!nameP.isEmpty() && !priceP.isEmpty() && !descriptionP.isEmpty()){
                int id = product.getId();
                Product product = new Product(id, imageP, nameP, priceP, descriptionP, categoryP);
                db.updateProduct(product);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            }
        }
        if(view == btnRemove){
            int id = product.getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Thong bao xoa");
            builder.setMessage("Ban co muon xoa " + product.getName() + " khong?");
            builder.setIcon(R.drawable.error);
            builder.setPositiveButton("Co", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    db.deleteProduct(id);
                    finish();
                }
            });
            builder.setNegativeButton("Khong", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if(view == btnCancel){
            finish();
        }
    }
}